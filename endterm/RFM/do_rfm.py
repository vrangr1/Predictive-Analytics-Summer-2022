import csv
import os
import pandas as pd
from datetime import timedelta
import matplotlib.pyplot as plt
import squarify

def do_rfm(filename):
    online = pd.read_csv(filename)
    print('{:,} rows; {:,} columns'.format(online.shape[0], online.shape[1]))
    print('{:,} transactions don\'t have a customer id'.format(online[online.customerID.isnull()].shape[0]))
    # print('Transactions timeframe from {} to {}'.format(online['InvoiceDate'].min(), online['InvoiceDate'].max()))


if __name__ == '__main__':
    filename = 'filled_missing_values.csv'
    # online = pd.read_csv('../data.csv', encoding = "ISO-8859-1")
    do_rfm(filename)
    pass