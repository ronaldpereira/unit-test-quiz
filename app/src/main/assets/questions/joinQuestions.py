#!/usr/bin/python3

from sys import argv
import json

with open('_allQuestions.json', 'w') as output:
    output.write('{\n')
    for file in argv[1:]:
        with open(file, 'r') as f:
            line = '\t' + '"' + f.name + '":'
            line += json.dumps(json.load(f), indent=8)
            line = line[:-1]
            line += ('\t},\n' if f.name != argv[-1] else '\t}')
            output.write(line)
            f.close()
    output.write('\n}')
