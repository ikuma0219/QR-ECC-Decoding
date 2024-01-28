##モジュールを輝度値毎に分類して、その数を表にする。

import glob
import cv2
from matplotlib import pyplot as plt
import numpy as np
import pandas as pd
from pandas.plotting import table
import re

#ファイルを番号順にソートするためのkey
def atoi(text):
    return int(text) if text.isdigit() else text

def natural_keys(text):
    return [ atoi(c) for c in re.split(r'(\d+)', text) ]

box_size = 8
margin = 2

#フォルダからQRコードを読み込み
originalqr = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\originalqr\\*.png')
denoiseqr = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\denoiseqr\\*.png')
errormod = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\errormodplace\\*.png')
errorsym = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\errorsymplace\\*.png')
originalqr = sorted(originalqr , key = natural_keys)
denoiseqr = sorted(denoiseqr , key = natural_keys)
errormod = sorted(errormod , key = natural_keys)
errorsym = sorted(errorsym , key = natural_keys)


#QRコード画像⇒numpy
originalqr_list = []
denoiseqr_list = []
errormod_list = []
errorsym_list = []

for i in range(len(denoiseqr)):
    originalqr_ndarray = cv2.imread(originalqr[i], cv2.IMREAD_GRAYSCALE)
    denoiseqr_ndarray = cv2.imread(denoiseqr[i], cv2.IMREAD_GRAYSCALE)
    errormod_ndarray = cv2.imread(errormod[i], cv2.IMREAD_GRAYSCALE)
    errorsym_ndarray = cv2.imread(errorsym[i], cv2.IMREAD_GRAYSCALE)

    originalqr_ndarray_expanded = np.expand_dims(originalqr_ndarray,axis=0)
    denoiseqr_ndarray_expanded = np.expand_dims(denoiseqr_ndarray,axis=0)
    errormod_ndarray_expanded = np.expand_dims(errormod_ndarray,axis=0)
    errorsym_ndarray_expanded = np.expand_dims(errorsym_ndarray,axis=0)

    originalqr_list.append(originalqr_ndarray_expanded)
    denoiseqr_list.append(denoiseqr_ndarray_expanded)
    errormod_list.append(errormod_ndarray_expanded)
    errorsym_list.append(errorsym_ndarray_expanded)

originalqr_np_sum = np.concatenate(originalqr_list,axis=0)
denoiseqr_np_sum = np.concatenate(denoiseqr_list,axis=0)
errormod_np_sum = np.concatenate(errormod_list,axis=0)
errorsym_np_sum = np.concatenate(errorsym_list,axis=0)


for num in range(denoiseqr_np_sum.shape[0]):

    originalqr_gray =  originalqr_np_sum[num]
    denoiseqr_gray =  denoiseqr_np_sum[num]
    errormod_gray = errormod_np_sum[num]

    # モジュールの輝度値を分類してカウントするための変数
    modlumi_0_20 = 0
    modlumi_20_40 = 0
    modlumi_40_60 = 0
    modlumi_60_80 = 0
    modlumi_80_100 = 0
    modlumi_100_120 = 0
    modlumi_120_140 = 0
    modlumi_140_160 = 0
    modlumi_160_180 = 0
    modlumi_180_200 = 0
    modlumi_200_220 = 0
    modlumi_220_240 = 0
    modlumi_240_255 = 0

    errormodlumi_0_20 = 0
    errormodlumi_20_40 = 0
    errormodlumi_40_60 = 0
    errormodlumi_60_80 = 0
    errormodlumi_80_100 = 0
    errormodlumi_100_120 = 0
    errormodlumi_120_140 = 0
    errormodlumi_140_160 = 0
    errormodlumi_160_180 = 0
    errormodlumi_180_200 = 0
    errormodlumi_200_220 = 0
    errormodlumi_220_240 = 0
    errormodlumi_240_255 = 0

    correctmodlumi_0_20 = 0
    correctmodlumi_20_40 = 0
    correctmodlumi_40_60 = 0
    correctmodlumi_60_80 = 0
    correctmodlumi_80_100 = 0
    correctmodlumi_100_120 = 0
    correctmodlumi_120_140 = 0
    correctmodlumi_140_160 = 0
    correctmodlumi_160_180 = 0
    correctmodlumi_180_200 = 0
    correctmodlumi_200_220 = 0
    correctmodlumi_220_240 = 0
    correctmodlumi_240_255 = 0

    modlumi_list = []
    errormodlumi_list = []
    correctmodlumi_list = []


    for i in range(int(box_size * margin + box_size/2), int(errormod_gray.shape[0] - box_size * margin), box_size):
        for j in range(int(box_size * margin + box_size/2), int(errormod_gray.shape[1] - box_size * margin), box_size):

            modlumi = denoiseqr_gray[i,j]

            if(0 <= modlumi < 20):
                modlumi_0_20 += 1 

                if(errormod_gray[i,j] == 0):
                    errormodlumi_0_20 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_0_20 += 1

            elif (20 <= modlumi < 40):
                modlumi_20_40 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_20_40 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_20_40 += 1
                
            elif (40 <= modlumi < 60):
                modlumi_40_60 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_40_60 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_40_60 += 1

            elif (60 <= modlumi < 80):
                modlumi_60_80 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_60_80 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_60_80 += 1

            elif (80 <= modlumi < 100):
                modlumi_80_100 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_80_100 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_80_100 += 1

            elif (100 <= modlumi < 120):
                modlumi_100_120 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_100_120 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_100_120 += 1

            elif (120 <= modlumi < 140):
                modlumi_120_140 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_120_140 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_120_140 += 1

            elif (140 <= modlumi < 160):
                modlumi_140_160 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_140_160 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_140_160 += 1

            elif (160 <= modlumi < 180):
                modlumi_160_180 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_160_180 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_160_180 += 1

            elif (180 <= modlumi < 200):
                modlumi_180_200 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_180_200 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_180_200 += 1

            elif (200 <= modlumi < 220):
                modlumi_200_220 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_200_220 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_200_220 += 1

            elif (220 <= modlumi < 240):
                modlumi_220_240 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_220_240 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_220_240 += 1

            else:
                modlumi_240_255 += 1

                if(errormod_gray[i,j] == 0):
                    errormodlumi_240_255 += 1
                elif (errormod_gray[i,j] == 255):
                    correctmodlumi_240_255 += 1
                
    
    modlumi_list.append(modlumi_0_20)
    modlumi_list.append(modlumi_20_40)
    modlumi_list.append(modlumi_40_60)
    modlumi_list.append(modlumi_60_80)
    modlumi_list.append(modlumi_80_100)
    modlumi_list.append(modlumi_100_120)
    modlumi_list.append(modlumi_120_140)
    modlumi_list.append(modlumi_140_160)
    modlumi_list.append(modlumi_160_180)
    modlumi_list.append(modlumi_180_200)
    modlumi_list.append(modlumi_200_220)
    modlumi_list.append(modlumi_220_240)
    modlumi_list.append(modlumi_240_255)

    errormodlumi_list.append(errormodlumi_0_20)
    errormodlumi_list.append(errormodlumi_20_40)
    errormodlumi_list.append(errormodlumi_40_60)
    errormodlumi_list.append(errormodlumi_60_80)
    errormodlumi_list.append(errormodlumi_80_100)
    errormodlumi_list.append(errormodlumi_100_120)
    errormodlumi_list.append(errormodlumi_120_140)
    errormodlumi_list.append(errormodlumi_140_160)
    errormodlumi_list.append(errormodlumi_160_180)
    errormodlumi_list.append(errormodlumi_180_200)
    errormodlumi_list.append(errormodlumi_200_220)
    errormodlumi_list.append(errormodlumi_220_240)
    errormodlumi_list.append(errormodlumi_240_255)

    correctmodlumi_list.append(correctmodlumi_0_20)
    correctmodlumi_list.append(correctmodlumi_20_40)
    correctmodlumi_list.append(correctmodlumi_40_60)
    correctmodlumi_list.append(correctmodlumi_60_80)
    correctmodlumi_list.append(correctmodlumi_80_100)
    correctmodlumi_list.append(correctmodlumi_100_120)
    correctmodlumi_list.append(correctmodlumi_120_140)
    correctmodlumi_list.append(correctmodlumi_140_160)
    correctmodlumi_list.append(correctmodlumi_160_180)
    correctmodlumi_list.append(correctmodlumi_180_200)
    correctmodlumi_list.append(correctmodlumi_200_220)
    correctmodlumi_list.append(correctmodlumi_220_240)
    correctmodlumi_list.append(correctmodlumi_240_255)

   
    plt.rcParams['font.family'] = 'MS Gothic'
    
    index1 = ["0-20", "20-40", "40-60", "60-80", "80-100", "100-120", "120-140", 
              "140-160", "160-180", "180-200", "200-220", "220-240", "240-255"]
    columns1 =["モジュール"]
    df = pd.DataFrame(data=modlumi_list, index=index1, columns=columns1)
    df["誤ったモジュール"] = errormodlumi_list
    df["正しいモジュール"] = correctmodlumi_list
    

    fig, ax = plt.subplots(figsize=(7,5))
    ax.axis('off')
    ax.axis('tight')
    ax.table(
        cellText = df.values,
        rowLabels = df.index,
        colLabels = df.columns,
        loc = 'center',
        bbox = [0, 0, 1, 1],
    )

    storepath = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\modlumidata\\" + str(num) + ".png"
    plt.savefig(storepath,   bbox_inches="tight")
   

