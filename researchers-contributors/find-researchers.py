#!/usr/bin/env python3

import argparse
import sys
import os

import pandas as pd
import numpy as np
import math
import scholarly

import progressbar


parser = argparse.ArgumentParser(prog = 'Find Researchers')

parser.add_argument('-c', required = True, dest = 'commits_directory')
parser.add_argument('-s', required = True, dest = 'saved_dataset')

arguments = parser.parse_args(sys.argv[1:])
                                 

commits_directory = arguments.commits_directory
print('Drill results directory: "' + commits_directory + '".')

commits_files = os.listdir(commits_directory)
print('Are going to be used:', commits_files)

commits_files_paths = map(lambda name: commits_directory + '/' + name, commits_files)


print()


print('Reuniting all datasets into a single one.')
commits = pd.concat(map(lambda path: pd.read_csv(path), commits_files_paths))
commits.reset_index(drop = True, inplace = True)


contributors = pd.DataFrame({'NAME': commits['AUTHOR_NAME'].unique()})
print('There is', len(contributors), 'unique contributors in the analyzed projects.')
contributors['EMAILS'] = contributors['NAME'].apply(lambda _: [])

contributors.set_index('NAME', inplace = True)


print()


print('Looking for contributors emails.')
with progressbar.ProgressBar(max_value = len(commits)) as bar:
    for index, row in commits.iterrows():
        contributors['EMAILS'][row['AUTHOR_NAME']].append(row['AUTHOR_EMAIL'])

        bar.update(index)


print()


contributors_status = pd.DataFrame({'NAME': contributors.index})

if os.path.isfile(arguments.saved_dataset):
    print('Reading saved contributors statuses in: "' + arguments.saved_dataset + '".')
    
    known_contributors = pd.read_csv(arguments.saved_dataset)

    contributors_status = contributors_status.merge(known_contributors, on = 'NAME', how = 'outer')
else:
    print('Cannot not find file "' + arguments.saved_dataset + '". Starting from zero.')
    
    contributors_status['HAS_RESEARCHER_EMAIL'] = np.NaN
    contributors_status['HAS_PUBLICATION'] = np.NaN


print()


print('Determining which contributor has at least one researcher email.')

def address_domain(address):
    split = address.split('@')
    
    if len(split) == 2:
        return split[1]
    else:
        return address

researcher_domains = ['inria.fr', 'cnes.fr', 'normalesup.org', 'ensta.org', 'jnphilipp.org',
                    'vene.ro', 'cern.ch', 'uva.nl', 'cea.fr', 'heig-vd.ch', 'wxs.ca', 'smerity.com',
                    'nsup.org', 'esciencecenter.nl', 'centraliens.net', 'acm.org', 'fu-berlin.de', 
                    'fit.vutbr.cz', 'hec.ca', 'barvinograd.com', 'jakelee.net', 'maluuba.com',
                    'nicta.com.au', 'poli.ufrj.br', 'barkalov.ru', 'allenai.org', 'usherbrooke.ca',
                    'taehoonlee.com', 'bioinf.jku.at', 'nerdluecht.de', 'yosinski.com', 'tum.de',
                    'enlnt.com', 'kottalam.net', 'jan-schlueter.de', 'iupr.com', 'uoguelph.ca',
                    'marcodena.it', 'esimon.eu', 'stophr.be', 'jan-matthis.de', 'josephpcohen.com',
                    'cwi.nl']
researcher_domains_ends = ['edu', 'ens-cachan.fr', 'intra.cea.fr', 'ntnu.no', 'umontreal.ca', 'mcgill.ca', 'epita.fr']
researcher_domains_contents = ['.ca.', 'research', '.ac.', '.uu.', 'edu', 'student', 'uni', 'etu']

def is_researcher_address(domain):
    for end in researcher_domains_ends:
        if domain.endswith(end):
            return True
    
    for content in researcher_domains_contents:
        if content in domain:
            return True
        
    return domain in researcher_domains

def has_one_researcher_email(contributor_name):
    global contributors

    for domain in list(map(address_domain, contributors['EMAIL'][contributor_name])):
        if is_researcher_address(domain):
            return True
    
    return False

def has_researcher_email(row):
    if math.isnan(row['HAS_RESEARCHER_EMAIL']):
        row['HAS_RESEARCHER_EMAIL'] = has_one_researcher_email(row['NAME'])
    
    return row

contributors_status = contributors_status.apply(has_researcher_email, axis = 1)

print('Contributors with a researcher email:', len(contributors_status[contributors_status['HAS_RESEARCHER_EMAIL'] == True]), '(out of', len(contributors_status), 'contributors).')
print('Contributors with no researcher email:', len(contributors_status[contributors_status['HAS_RESEARCHER_EMAIL'] == False]), '(out of', len(contributors_status), 'contributors).')


print()


print('Determining which contributors published at least once.')

must_be_fetched = contributors_status['HAS_PUBLICATION'].isnull().value_counts()[True]
print(must_be_fetched, 'contributor(s) are going to be fetched.')


with progressbar.ProgressBar(max_value = len(contributors_status)) as bar:
    current = 0
    is_captcha_activated = False
    
    def is_researcher_author(row):
        global current
        
        try:
            if math.isnan(row['HAS_PUBLICATION']):
                row['HAS_PUBLICATION'] = is_researcher_author_by_name(row['NAME'])
            
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

    contributors_status = contributors_status.apply(is_researcher_author, axis = 1)

print('Contributors who published at least once:', len(contributors_status[contributors_status['HAS_PUBLICATION'] == True]), '(out of', len(contributors_status), 'contributors).')
print('Contributors who never published:', len(contributors_status[contributors_status['HAS_PUBLICATION'] == False]), '(out of', len(contributors_status), 'contributors).')
    
print()

print('Saving results in "' + arguments.saved_dataset + '".')
contributors_status.to_csv(arguments.saved_dataset, index = False)
