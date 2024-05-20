import ast
from abc import ABC, abstractmethod
from collections import deque


class Listener(ABC):
    @abstractmethod
    def change(self):
        pass


class Cell(Listener):
    def __init__(self, exp, sheet):
        self.exp = exp
        self.sheet = sheet
        if isinstance(self.exp, int):
            self.value = self.exp
        else:
            self.value = self.eval_expression(self.exp, self.sheet)
        self.listeners = deque()
        self.references = deque()

    def eval_expression(self, exp, variables={}):
        def _eval(node):
            if isinstance(node, ast.Num):
                return node.n
            elif isinstance(node, ast.Name):
                cell = variables.cell(node.id)
                if self not in cell.listeners:
                    cell.addListener(self)
                if cell not in self.references:
                    self.references.append(cell)
                return cell.value
            elif isinstance(node, ast.BinOp):
                return _eval(node.left) + _eval(node.right)
            else:
                raise Exception('Unsupported type {}'.format(node))

        node = ast.parse(exp, mode='eval')
        return _eval(node.body)

    def change(self):
        if isinstance(self.exp, int):
            self.value = self.exp
        else:
            self.value = self.eval_expression(self.exp, self.sheet)

    def addListener(self, l):
        self.listeners.append(l)

    def removeListener(self, l):
        self.listeners.remove(l)

    def inform(self):
        for c in self.listeners:
            c.change()


class Sheet:
    def __init__(self, x, y):
        self.sheet = [[None] * y for _ in range(x)]
        self.names = [[''] * y for _ in range(x)]
        self.setCellNames(x, y)

    def setCellNames(self, x, y):
        columnChar = 'A'
        for i in range(x):
            for j in range(y):
                self.names[i][j] = columnChar + str(j + 1)
                self.sheet[i][j] = Cell(0, self)
            columnChar = chr(ord(columnChar) + 1)

    def print(self):
        rows = len(self.sheet)
        cols = len(self.sheet[0])

        max_cell_width = max(len(str(cell.value)) for row in self.sheet for cell in row)

        # Print column headers
        print("  |", end="")
        for i in range(1, cols + 1):
            print(f" {i:{max_cell_width}} |", end="")
        print()

        print("  +", end="")
        for _ in range(cols):
            print(f"{'-' * (max_cell_width + 2)}+", end="")
        print()

        for i, row in enumerate(self.sheet):
            print(chr(ord('A') + i) + " |", end="")
            for cell in row:
                print(f" {cell.value:{max_cell_width}} |", end="")
            print()

        print("  +", end="")
        for _ in range(cols):
            print(f"{'-' * (max_cell_width + 2)}+", end="")
        print()

    def cell(self, ref):
        for i in range(len(self.sheet)):
            for j in range(len(self.sheet[i])):
                if self.names[i][j] == ref:
                    return self.sheet[i][j]
        return None

    def set(self, ref, content):
        element = self.cell(ref)
        i = int(ord(ref[0]) - ord('A'))
        j = int(ref[1]) - 1
        if isinstance(content, int):
            if element:
                element.exp = content
                element.change()
                self.loopCheck(element)
                element.inform()
        else:
            refs = self.getrefs(content)
            if refs:
                for r in refs:
                    if element == r:
                        raise RuntimeError("Circular dependency detected.")
                if element:
                    element.exp = content
                    element.change()
                    self.loopCheck(element)
                    element.inform()

    def getrefs(self, content):
        refs = []
        for c in content.split('\\s*[+\\-*/]\\s*'):
            c = c.strip()
            if c[0].isalpha():
                refs.append(self.cell(c))
        return refs

    def loopCheck(self, c):
        stack = deque([c])
        visited = []
        while stack:
            el = stack.pop()
            if el in visited:
                raise RuntimeError("Circular dependency detected.")
            visited.append(el)
            for s in el.references:
                stack.append(s)

    def evaluate(self, cell):
        cell.evaluate()


def main():
    s = Sheet(5, 5)

    s.set('A1', 1)
    s.set('A2', 2)
    s.set('B1', 3)
    s.set('B2', 4)
    s.set('C4', 5)
    s.set('C5', 6)
    s.print()
    print()

    s.set('A3', 'A1+A2')
    s.print()
    print()
    s.set('A1', 10)
    s.print()
    print()

    #try:
        #s.set('A1', 'A3')
    #except RuntimeError as e:
     #   print("Caught exception:", e)
   # s.print()
   # print()


if __name__ == "__main__":
    main()
