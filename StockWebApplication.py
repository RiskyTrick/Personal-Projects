#python Stock view program on web

import streamlit as st
import pandas as pd
from PIL import Image

#code start

st.write("""
#Stock Market Web Application
**Visualise** data on stocks! 
Developed by: **RiskyTrick**!!
""")
image = Image.open("C:/Users/Asus/Desktop/stock app/stockimage.jpg")
st.image(image,use_column_width=True)

#to create a side division on web
st.sidebar.header('User Input Section')

#to get data
def get_input():
    start_date=st.sidebar.text_input("Start Date","01-31-1990")
    end_date=st.sidebar.text_input("End Date","02-20-2020")
    stock_symbol=st.sidebar.text_input("Stock Symbol","AMZN")
    return start_date,end_date,stock_symbol

#to get company name
def get_company_name(symbol):
    if symbol=='AMZN':
        return 'Amazon'
    elif symbol=='TSLA':
        return 'TESLA'
    elif symbol == 'GOOG':
        return 'GOOGLE'
    else:
        'None'
def get_data(symbol,start,end):

    #loading data and creating data frames
    if symbol.upper()=='AMZN':
        df=pd.read_csv("C:/Users/Asus/Desktop/stock app/AMZN.csv")
    elif symbol.upper() == 'TSLA':
        df = pd.read_csv("C:/Users/Asus/Desktop/stock app/TSLA.csv")
    elif symbol.upper() == 'GOOG':
        df = pd.read_csv("C:/Users/Asus/Desktop/stock app/GOOG.csv")

    start = pd.to_datetime(start)
    end=pd.to_datetime(end)
    start_row=0
    end_row=0

    for i in range(0,len(df)):
        if start<=pd.to_datetime(df['Date'][i]):
            start_row=i
            break

    for j in range(0,len(df)):
        if end>= pd.to_datetime(df['Date'][len(df)-1-j]):
            end_row = len(df)-1-j
            break

    df=df.set_index(pd.DatetimeIndex(df['Date'].values))
    return df.iloc[start_row:end_row+1,:]

#getting user input
start,end,symbol=get_input()

df=get_data(symbol,start,end)

company_name=get_company_name(symbol.upper())

#visualising data
st.header(company_name+"Closing Price\n")
st.line_chart(df['Close'])

st.header(company_name+"Volume\n")
st.line_chart(df['Volume'])

st.header('Data Stats')
st.write(df.describe())
