# Let us import some awesome modules.
import pandas as pd
import numpy as np
import scholarly
import progressbar
import os.path
import math


def load_project_dataset(project_name):
    dataset = pd.read_csv('study-results/' + project_name + '/exploration.csv')

    dataset['PROJECT'] = project_name

    return dataset
    
# List of projects.
projects = ['scikit-learn', 'keras', 'theano']

# Build a merged dataset for all commits of all projects.
commits = pd.concat(list(map(load_project_dataset, projects)))


# Prepare a dataset for each contributors.
contributors = pd.DataFrame({'NAME': commits['AUTHOR_NAME'].unique()})

contributors['COMMITS_DATES'] = contributors['NAME'].apply(lambda ignored: [])
contributors['EMAILS'] = contributors['NAME'].apply(lambda ignored: [])
contributors['ADDED_LINES'] = contributors['NAME'].apply(lambda ignored: [])
contributors['DELETED_LINES'] = contributors['NAME'].apply(lambda ignored: [])

contributors.set_index('NAME', inplace = True)

# Fill the contributors dataset with values from the commits dataset.
for index, row in commits.iterrows():
    name = row['AUTHOR_NAME']

    contributors['COMMITS_DATES'][name].append(row['TIMESTAMP'])
    contributors['EMAILS'][name].append(row['AUTHOR_EMAIL'])
    contributors['ADDED_LINES'][name].append(row['ADDED_LINES'])
    contributors['DELETED_LINES'][name].append(row['DELETED_LINES'])


# Compute statistics for each contributors.
contributors_stats = pd.DataFrame({'NAME': contributors.index})
contributors_stats['NUMBER_OF_COMMITS'] = 0
contributors_stats['ADDED_LINES'] = 0
contributors_stats['DELETED_LINES'] = 0

def compute_contributor_stats(row):
    global contributors
    
    name = row['NAME']

    row['NUMBER_OF_COMMITS'] = len(contributors['COMMITS_DATES'][name])
    row['ADDED_LINES'] = sum(contributors['ADDED_LINES'][name])
    row['DELETED_LINES'] = sum(contributors['DELETED_LINES'][name])

    return row

contributors_stats = contributors_stats.apply(compute_contributor_stats, axis = 1)

contributors_stats.to_csv('study-results/contributors-statistics.csv', index = False)


# Search the status of each researcher.
contributors_status = pd.DataFrame({'NAME': contributors.index})

if os.path.isfile('study-results/contributors.csv'):
    known_contributors = pd.read_csv('study-results/contributors.csv')

    contributors_status = contributors_status.merge(known_contributors, on = 'NAME', how = 'outer')
else:
    contributors_status['HAS_RESEARCHER_EMAIL'] = np.NaN
    contributors_status['HAS_PUBLICATION'] = np.NaN
    

# Fill the 'HAS_RESEARCHER_EMAIL' column.
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


# Fill the 'HAS_PUBLICATION' column.
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


contributors_status.to_csv('study-results/contributors-status.csv', index = False)
