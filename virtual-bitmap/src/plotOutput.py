import csv
import matplotlib.pyplot as plt
from pylab import rcParams
import matplotlib.lines as mlines
import matplotlib.transforms as mtransforms


f = open("output.csv")
reader = csv.reader(f,delimiter=",")
x=[]
y=[]

i=0;
for rows in reader:
    x.append(int(rows[0]))
    y.append(int(rows[1]))
    i+=1

rcParams['figure.figsize'] = 12, 10

_ , subplt = plt.subplots()
subplt.set_xlabel('actual spread')
subplt.set_ylabel('estimated spread')

subplt.scatter(x,y)
# line = mlines.Line2D([0, 1], [0, 1], color='red')
transform = subplt.transAxes
# line.set_transform(transform)
# subplt.add_line(line)
plt.show()