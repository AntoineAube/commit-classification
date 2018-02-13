import requests
import math
import statsmodels.api as sm
import seaborn as sns
import numpy as np
from scipy.stats import norm as nrm, mstats

from itertools import groupby
from datetime import datetime as dt

from repos import *
from utils import *
from perform_mktests import *
from perform_sonarqube_analysis import *

repos = get_repos()

perform_the_analysis(repos)
perform_mktests(repos)