##QRコードについて誤って読み取られたモジュールやシンボルを図示する

import glob
import cv2
from matplotlib import pyplot as plt
import numpy as np

import re

#ファイルを番号順にソートするためのkey
def atoi(text):
    return int(text) if text.isdigit() else text

def natural_keys(text):
    return [ atoi(c) for c in re.split(r'(\d+)', text) ]

box_size = 8
margin = 2

#フォルダからQRコードを読み込み
originalqr = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\originalqr\\*.png')
noiseqr = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\noiseqr\\*.png')
denoiseqr = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\denoiseqr\\*.png')
denoisebinqr = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\denoisebinqr\\*.png')
errormod = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\errormodplace\\*.png')
errorsym = glob.glob('C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\errorsymplace\\*.png')
originalqr = sorted(originalqr , key = natural_keys)
noiseqr = sorted(noiseqr , key = natural_keys)
denoiseqr = sorted(denoiseqr , key = natural_keys)
denoisebinqr = sorted(denoisebinqr , key = natural_keys)
errormod = sorted(errormod , key = natural_keys)
errorsym = sorted(errorsym , key = natural_keys)


#QRコード画像⇒numpy
originalqr_list = []
noiseqr_list = []
denoiseqr_list = []
denoisebinqr_list = []
errormod_list = []
errorsym_list = []

for i in range(len(denoiseqr)):
    originalqr_ndarray = cv2.imread(originalqr[i], cv2.IMREAD_GRAYSCALE)
    noiseqr_ndarray = cv2.imread(noiseqr[i], cv2.IMREAD_GRAYSCALE)
    denoiseqr_ndarray = cv2.imread(denoiseqr[i], cv2.IMREAD_GRAYSCALE)
    denoisebinqr_ndarray = cv2.imread(denoisebinqr[i], cv2.IMREAD_GRAYSCALE)
    errormod_ndarray = cv2.imread(errormod[i], cv2.IMREAD_GRAYSCALE)
    errorsym_ndarray = cv2.imread(errorsym[i], cv2.IMREAD_GRAYSCALE)

    originalqr_ndarray_expanded = np.expand_dims(originalqr_ndarray,axis=0)
    noiseqr_ndarray_expanded = np.expand_dims(noiseqr_ndarray,axis=0)
    denoiseqr_ndarray_expanded = np.expand_dims(denoiseqr_ndarray,axis=0)
    denoisebinqr_ndarray_expanded = np.expand_dims(denoisebinqr_ndarray,axis=0)
    errormod_ndarray_expanded = np.expand_dims(errormod_ndarray,axis=0)
    errorsym_ndarray_expanded = np.expand_dims(errorsym_ndarray,axis=0)

    originalqr_list.append(originalqr_ndarray_expanded)
    noiseqr_list.append(noiseqr_ndarray_expanded)
    denoiseqr_list.append(denoiseqr_ndarray_expanded)
    denoisebinqr_list.append(denoisebinqr_ndarray_expanded)
    errormod_list.append(errormod_ndarray_expanded)
    errorsym_list.append(errorsym_ndarray_expanded)

originalqr_np_sum = np.concatenate(originalqr_list,axis=0)
noiseqr_np_sum = np.concatenate(noiseqr_list,axis=0)
denoiseqr_np_sum = np.concatenate(denoiseqr_list,axis=0)
denoisebinqr_np_sum = np.concatenate(denoisebinqr_list,axis=0)
errormod_np_sum = np.concatenate(errormod_list,axis=0)
errorsym_np_sum = np.concatenate(errorsym_list,axis=0)


for num in range(denoiseqr_np_sum.shape[0]):

    originalqr_gray =  originalqr_np_sum[num]
    noiseqr_gray =  noiseqr_np_sum[num]
    denoiseqr_gray =  denoiseqr_np_sum[num]
    denoisebinqr_gray =  denoisebinqr_np_sum[num]
    errormod_gray = errormod_np_sum[num]
    errorsym_gray = errorsym_np_sum[num]

    #誤ったモジュール、シンボルを塗るための画像
    errormod_paint = cv2.cvtColor(denoiseqr_gray, cv2.COLOR_GRAY2BGR)
    errorsym_paint = cv2.cvtColor(denoiseqr_gray, cv2.COLOR_GRAY2BGR)


    error_mod_ext = np.copy(denoiseqr_gray)


    for i in range(errormod_gray.shape[0]):
        for j in range(errormod_gray.shape[1]):
            if(errormod_gray[i,j] == 0):
                error_mod_ext[i,j] = denoiseqr_gray[i,j]
                errormod_paint[i,j] = 255, 0, 0
            elif (errormod_gray[i,j] == 255):
                error_mod_ext[i,j] = 255
            else:
                print("1~254")

            if(errorsym_gray[i,j] == 0):
                errorsym_paint[i,j] = 255, 0, 0
    
    plt.figure(figsize=(80, 40))
    plt.subplots_adjust(wspace=0.1, hspace=0.1)
    ax = plt.subplot(2, 4, 1)
    plt.imshow(originalqr_gray)
    plt.gray()
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)

    ax = plt.subplot(2, 4, 2)
    plt.imshow(noiseqr_gray)
    plt.gray()
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)

    ax = plt.subplot(2, 4, 3)
    plt.imshow(denoiseqr_gray)
    plt.gray()
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)

    ax = plt.subplot(2, 4, 4)
    plt.imshow(denoisebinqr_gray)
    plt.gray()
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)

    ax = plt.subplot(2, 4, 5)
    plt.imshow(errormod_paint)
    plt.gray()
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)

    ax = plt.subplot(2, 4, 6)
    plt.imshow(error_mod_ext)
    plt.gray()
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)

    ax = plt.subplot(2, 4, 7)
    plt.imshow(errorsym_paint)
    plt.gray()
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)

    plt.subplots_adjust(left=0, right=1, bottom=0, top=1)

    storepath = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\errordatasum\\" + str(num) + ".png"
    plt.savefig(storepath,  bbox_inches="tight")
   
             
