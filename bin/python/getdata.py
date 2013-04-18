import sys
import os

path = sys.argv[1]
a = sys.argv[2]
b = []
if len(a)==2:
    b.append(a[0]+'.txt')
    b.append(a[1]+'.txt')
else:
    b.append(a+'.txt')

attri = ['ATTENTION','MEDITATION','RAW','DELTA','THETA','ALPHA1','ALPHA2','BETA1','BETA2','GAMMA1','GAMMA2','CONFUSION']
trText = []
teText = []
Nume = [[],[],[],[],[],[],[],[],[],[],[],[]]

def findpassage(path):
    f = []
    for (d, p, fl) in os.walk(path):
        f.extend(fl)
        break
    return f

def getArff(filename):
    if filename in b:
        getTest(filename)
    else:
        getTrain(filename)

def getTest(title):
    text = [line.strip() for line in open(path+title)]
    for i in range(len(text)/4, len(text)*3/4):
        s = ""
        w = text[i].split(',')
        for j in range(3,len(w)):
            s+=str(int(float(w[j])))+','
            if int(float(w[j])) not in Nume[j-3]:
                Nume[j-3].append(int(float(w[j])))
        teText.append(s[:-1])

def getTrain(title):
    text = [line.strip() for line in open(path+title)]
    for i in range(len(text)/4, len(text)*3/4):
        s = ""
        w = text[i].split(',')
        for j in range(3,len(w)):
            s+=str(int(float(w[j])))+','
            if int(float(w[j])) not in Nume[j-3]:
                Nume[j-3].append(int(float(w[j])))
        trText.append(s[:-1])

def outTest():
    f = open('testingdata/testdata.arff', 'w')
    f.writelines('@relation 2013-03-24-weka.filters.unsupervised.attribute.NumericToNominal-Rfirst-last\n\n')
    for i in range(12):
        l = sorted(Nume[i])
        s = str(l)[1:-1]
        f.writelines('@attribute '+attri[i]+' {'+s+'}\n')
    f.writelines('\n')
    f.writelines('@data\n')
    for i in teText:
        f.writelines(i+'\n')

def outTrain():
    f = open('trainingdata/traindata.arff', 'w')
    f.writelines('@relation 2013-03-24-weka.filters.unsupervised.attribute.NumericToNominal-Rfirst-last\n\n')
    for i in range(12):
        l = sorted(Nume[i])
        s = str(l)[1:-1]
        f.writelines('@attribute '+attri[i]+' {'+s+'}\n')
    f.writelines('\n')
    f.writelines('@data\n')
    for i in trText:
        f.writelines(i+'\n')

print "do I start?"
titles = findpassage(path)
for t in titles:
    getArff(t)
outTest()
outTrain()