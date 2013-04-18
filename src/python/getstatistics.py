import sys

text = [line.strip() for line in open('result/out.txt')]

count = 0
numo = 0
for line in text:
    if 'Correctly Classified Instances' in line:
        count+=100
        word = line.split()
        num = float(word[len(word)-2])
        numo+=num

print "average accuracy is "+str(numo/count)