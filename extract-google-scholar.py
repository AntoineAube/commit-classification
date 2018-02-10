#!/usr/bin/env python3

import pandas as pd
import sys
import math
import progressbar
import numpy as np

if len(sys.argv) < 2:
    print('[ERROR] Please specify the path to the contributors file')
    sys.exit(1)

file_path = sys.argv[1]

contributors = pd.read_csv(file_path)

must_be_fetched = contributors['HAS_PUBLICATIONS'].isnull().value_counts()[True]
print('There are', must_be_fetched, 'author(s) that are going to be fetched.')

print('')

# Fetch from Google Scholar.
with progressbar.ProgressBar(max_value = len(all_contributors)) as bar:
    current = 0
    is_captcha_activated = False
    
    def is_researcher_author(row):
        global current
        
        try:
            if math.isnan(row['HAS_PUBLICATIONS']):
                row['HAS_PUBLICATIONS'] = is_researcher_author_by_name(row['AUTHOR_NAME'])
            
            return row   
        finally:
            bar.update(current)
            current += 1
            
    def is_researcher_author_by_name(author_name):
        global is_captcha_activated
        
        if is_captcha_activated:
            return np.NaN
        
        if len(author_name.split()) < 2:
            # It is more likely a nickname, so we cannot conclude.
            return False
        else:
            try:
                author = next(scholarly.search_author(author_name.title()), None)

                return author != None
            except Exception as exception:
                is_captcha_activated = True
                return np.NaN

    all_contributors = all_contributors.apply(is_researcher_author, axis = 1)

print('')

print('The approach with Google Scholar concluded that', len(all_contributors[all_contributors['HAS_PUBLICATIONS'] == False]), 'contributors (out of', len(all_contributors), 'contributors) are not researchers.')
print('The approach with Google Scholar concluded that', len(all_contributors[all_contributors['HAS_PUBLICATIONS'] == True]), 'contributors (out of', len(all_contributors), 'contributors) are researchers.')

contributors.to_csv(file_path)
