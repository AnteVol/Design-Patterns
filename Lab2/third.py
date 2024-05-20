def mymax(iterable, key=lambda x: x):
  max_x=max_key=None
  for x in iterable:
      if(max_x == None or key(x) > max_key):
          max_key = key(x)
          max_x = x
  return max_x

maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
maxchar = mymax("Suncana strana ulicez")
maxstring = mymax([
  "Gle", "malu", "vocku", "poslije", "kise",
  "Puna", "je", "kapi", "pa", "ih", "njise"])

D={'burek':8, 'dafina':99, 'kroasan':121, 'buhtla':200}
maxDict = mymax(D, key=D.get)
maxDict1 = mymax(D, key=D.get)
nameSurnames = [('Ante', 'Volarević'), ('Luka', 'Žmak'), ('Marko', 'Čupić'), ('ante', 'Volarević')]
maxNameSurnames = mymax(nameSurnames)

print("Max int is: " + str(maxint))
print("Max char is: " + maxchar)
print("Max string is: " + maxstring)
print("Key with max value: "+ maxDict + ", value:" + str(D[maxDict]))
print("Max name surname pair is: " + maxNameSurnames[0] + " " + maxNameSurnames[1])
print(maxDict1)