import requests
import numpy as np

predict_request='{"signature_name": "serving_default", "instances":[{"inputs":%s}] }' %np.random.rand(224,224,3).tolist()
# print(predict_request)

response = requests.post(url="http://192.168.230.128:31212/function/mobilenet", data=predict_request)

response.raise_for_status()
print(response.text)
print(response.elapsed.total_seconds() * 1000)

