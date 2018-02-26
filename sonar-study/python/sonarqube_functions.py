from utils import *
from GitHubProject import *
import pandas as pd

def transform_old_to_new_format(cells):
    num_dates = len(cells[0]['history'])
    dates_values = {}
    measures_indexes = {}

    for measure in cells:
        measures_indexes[measure['metric']] = METRICS_LIST.index(measure['metric'])
    
    for date_value in cells[0]['history']:
        dates_values[date_value['date']] = [0 for x in range(metrics_list_length)]
    
    for measure in cells:
        for date_value in measure['history']:
            if 'value' in date_value:
                dates_values[date_value['date']][measures_indexes[measure['metric']]] = date_value['value']
            else:
                dates_values[date_value['date']][measures_indexes[measure['metric']]] = None
    
    new_cells = []

    for date, value in dates_values.items():
        new_cells.append({'d': date, 'v': value})
    
    return new_cells

# Functions for getting SonarQube timemachine metrics
def get_sonarqube_timemachine_metrics_DataFrame(ghp):
    json_data = get_sonarqube_timemachine_metrics(ghp)
    cells = get_cells(json_data)
    cells = transform_old_to_new_format(cells)
    return transform_timemachine_metrics_to_series(cells)

def get_sonarqube_timemachine_metrics(ghp):
    # print(ghp.timemachine_metrics_url + METRICS)
    return get_rest_response(ghp.timemachine_metrics_url + METRICS)

def get_cells(json_data):
    return json_data['measures']  # cells - metric values

def get_colls(json_data):
    return json_data[0]['cols']  # cols - Metrcs

def get_no_of_versions(cells):
    return len(cells)

def transform_timemachine_metrics_to_series(cells):
    date_range_index = get_date_range_index_for_timemachine_metrics(cells)
    df = pd.DataFrame(index=date_range_index)

    for metric_index in range(0, metrics_list_length):
        s = pd.Series(index=date_range_index)
        for item in cells:
            s[pd.Timestamp(item.get('d')).date()] = item.get('v')[metric_index]
        df[METRICS_LIST[metric_index]] = s
    return df


def get_date_range_index_for_timemachine_metrics(cells):
    project_dates = get_project_dates(cells)
    min_project_date = min(project_dates)
    max_project_date = max(project_dates)
    return pd.date_range(start=min_project_date, end=max_project_date, freq='D')

def get_project_dates(cells):
    dates = []
    for item in cells:
        dates.append(pd.Timestamp(item.get('d')).date())
    return dates