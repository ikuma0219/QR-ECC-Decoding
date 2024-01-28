# 間違ったモジュールとシンボルの数を示す棒グラフを作成


import matplotlib.pyplot as plt
import csv

# csvファイルを読み取って棒グラフに起こす、モジュールとシンボル二つ用
def csvgraph(filename1,filname2):

    with open(filename1, encoding='utf8', newline='') as f:
        csvreader = csv.reader(f)
        errormoduledata0 = [row for row in csvreader] 

    for i in range (len(errormoduledata0[0])):
        errormoduledata0[0][i] = int(errormoduledata0[0][i])

    errormoduledata = errormoduledata0[0]

    print(errormoduledata)

    with open(filename2, encoding='utf8', newline='') as f:
        csvreader = csv.reader(f)
        errorsymboldata0 = [row for row in csvreader] 

    for i in range (len(errorsymboldata0[0])):
        errorsymboldata0[0][i] = int(errorsymboldata0[0][i])

    errorsymboldata = errorsymboldata0[0]

    print(errorsymboldata)

    labels_mod = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]
    labels_sym = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]
    # 誤ったモジュールの数を示すグラフ
    plt.bar(labels_mod, errormoduledata)
    plt.xlabel('誤ったモジュールの数', fontsize=18, fontname="MS Gothic")
    plt.ylabel('QRコードの数', fontsize=18, fontname="MS Gothic")
    plt.legend(fontsize=18)
    plt.tick_params(labelsize=18)
    plt.tight_layout()
    plt.savefig("C:\\code\\QRreader\\qrreder\\AI2data\\error_module_data_8_1.png")
    plt.show()

    # 誤ったシンボルの数を示すグラフ
    plt.bar(labels_sym, errorsymboldata)
    plt.xlabel('誤ったシンボルの数', fontsize=18, fontname="MS Gothic")
    plt.ylabel('QRコードの数', fontsize=18, fontname="MS Gothic")
    plt.legend(fontsize=18)
    plt.tick_params(labelsize=18)
    plt.tight_layout()
    plt.savefig("C:\\code\\QRreader\\qrreder\\AI2data\\error_symbol_data_8_1.png")
    plt.show()




##main

# パスが違うので適宜変更
filename1 = 'C:\\code\\QRreader\\error_module_data.csv'
filename2 = 'C:\\code\\QRreader\\error_symbol_data.csv'

csvgraph(filename1, filename2)
