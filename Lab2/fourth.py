from random import normalvariate
from math import ceil

class DistributionTester:
    def __call__(self, list, action):
        list.sort()
        p = 10
        while p <= 90:
            print(str(p) + ". percentil = " + str(action(list, p)))
            p = p + 10
        return

class normal:
    def __init__(self, lowerBound, upperBound, step):
        self.lowerBound = lowerBound
        self.upperBound = upperBound
        self.step = step

    def normalFunction(self):
        list = []
        help = self.lowerBound
        while(help < self.upperBound):
            list.append(help)
            help = help + self.step
        return list

class random:
    def __init__(self, param, numberOfElements):
        self.param = param
        self.numberOfElements = numberOfElements
    def randomFunction(self):
        list = []
        counter = 0
        while counter < self.numberOfElements:
            list.append(normalvariate(self.param[0], self.param[1]))
            counter = counter + 1
        return list

class fibonacci:
    def __init__(self, numberOfElements):
        self.numberOfElements = numberOfElements

    def fibonacciFunction(self):
        list = []
        number1, number2 = 0, 1
        counter = 0
        while counter < self.numberOfElements:
            list.append(number1)
            number2 = number2 + number1
            number1 = number2
            counter = counter + 1
        return list

class nearestRank:
    def __call__ (self, list, p):
        N = len(list)
        n_p = p*N/100 + 0.5
        take = ceil(n_p) - 1
        return list[take]

class linearInterpolation:
    def __call__(self, list, p):
        N = len(list)
        p_i = [100*(counter - 0.5) / N for counter in range(1, N + 1)]
        for i in range(len(p_i)):
            if p_i[i] >= p:
                if i == 0:
                    return list[i]
                return list[i - 1] + N * (p - p_i[i - 1]) * (list[i] - list[i - 1]) / 100
        return None

if __name__=="__main__":

    fibonacci_instance = fibonacci(10)
    random_instance = random([10, 5], 50)
    normal_instance = normal(50, 100, 5)
    nr = nearestRank()
    lt = linearInterpolation()
    tester = DistributionTester()

    print("Fibonacci with NearRank")
    tester(fibonacci_instance.fibonacciFunction(), nr)
    print("Fibonacci with LinearInterpolation")
    tester(fibonacci_instance.fibonacciFunction(), lt)
    print("Random with LinearInterpolation")
    tester(random_instance.randomFunction(), lt)
    print("Normal with NearRank")
    tester(normal_instance.normalFunction(), nr)
    print("Normal with LinearInterpolation")
    tester(normal_instance.normalFunction(), lt)
