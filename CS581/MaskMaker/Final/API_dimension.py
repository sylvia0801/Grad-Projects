import cv2
import time
import json
import numpy as np
import urllib.error
import urllib.request
import pyzbar.pyzbar as pyzbar


# API

# Getting information of API
http_url = 'https://api-cn.faceplusplus.com/facepp/v3/detect'
key = "eZc3FxFPwA1Nw2PNk0xZZ5buBEudiTy9"
secret = "ar41mMNiOg64b1eDICbmW5EfBtaGoUGJ"

# Change file name
filepath = r'testpic3.jpg'

data = []

boundary = '----------%s' % hex(int(time.time() * 1000))
data = []
data.append('--%s' % boundary)
data.append('Content-Disposition: form-data; name="%s"\r\n' % 'api_key')
data.append(key)
data.append('--%s' % boundary)
data.append('Content-Disposition: form-data; name="%s"\r\n' % 'api_secret')
data.append(secret)
data.append('--%s' % boundary)
fr = open(filepath, 'rb')
data.append('Content-Disposition: form-data; name="%s"; filename=" "' % 'image_file')
data.append('Content-Type: %s\r\n' % 'application/octet-stream')
data.append(fr.read())
fr.close()
data.append('--%s' % boundary)
data.append('Content-Disposition: form-data; name="%s"\r\n' % 'return_landmark')
data.append('1')
data.append('--%s--\r\n' % boundary)

for i, d in enumerate(data):
    if isinstance(d, str):
        data[i] = d.encode('utf-8')

http_body = b'\r\n'.join(data)


# Functions

# Get points from human faces according to the API
def get_info(link, content):
	# build http request
	req = urllib.request.Request(url=link, data=content)
	# header
	req.add_header('Content-Type', 'multipart/form-data; boundary=%s' % boundary)
	try:
		# post data to server
		resp = urllib.request.urlopen(req, timeout=5)
		qrcont = resp.read()
		result = json.loads(qrcont.decode('utf-8'))
		return result
	except urllib.error.HTTPError as e:
		print(e.read().decode('utf-8'))
		return -1

# Identify and decodes the QR code in the picture.
def decode(im) : 
    # Finds all QR codes and bar codes
    decodedObjects = pyzbar.decode(im) 
    return decodedObjects
 
 # Retrieves pixel width of QR code. 
def retrieve_width(decodedObjects):
    # The QR code object should be instructed to be at the bottom of the image. The decoder reads
    # top to bottom, left to right. 
    pixelWidth = 0
    # Loop over all decoded objects
    for decodedObject in decodedObjects: 
        pixelWidth = decodedObject.rect.width
    
    return pixelWidth


# Read image

# API
# Read points from the picture uploaded
file = get_info(http_url, http_body)
with open('83points.json', 'w') as json_file:  
    json.dump(file, json_file)

with open('83points.json') as json_file:
	data = json.load(json_file)

left_face_points = {}
right_face_points = {}
face_points = {}
left_eye_points = {}
right_eye_points = {}
nose_points = {}

# Add points from json file to left face points
for i in range(1, 10):
	string = 'contour_left' + str(i)
	point = data['faces'][0]['landmark'][string]
	left_face_points[string] = point
# Add points from json file to right face points
for i in range(1, 10):
	string = 'contour_right' + str(i)
	point = data['faces'][0]['landmark'][string]
	right_face_points[string] = point
# Add points to whole face points
point = data['faces'][0]['landmark']['contour_chin']
face_points['contour_chin'] = point
face_points.update(left_face_points)
face_points.update(right_face_points)

eye_terms = ['_bottom', '_center', '_left_corner', '_lower_left_quarter', '_lower_right_quarter', '_right_corner', '_top', '_upper_left_quarter', '_upper_right_quarter']
# Add points from json file to left eye points
for term in eye_terms:
	string = 'left_eye' + term
	point = data['faces'][0]['landmark'][string]
	left_eye_points[string] = point
# Add points from json file to right eye points
for term in eye_terms:
	string = 'right_eye' + term
	point = data['faces'][0]['landmark'][string]
	right_eye_points[string] = point

# Add points from json file to nose points
nose_terms = ['nose_contour_left1', 'nose_contour_lower_middle', 'nose_contour_right1', 'nose_left', 'nose_right', 'nose_tip']
for term in nose_terms:
	point = data['faces'][0]['landmark'][term]
	nose_points[term] = point

result = {}
result.update(face_points)
result.update(left_eye_points)
result.update(right_eye_points)
result.update(nose_points)

# QR Code
im = cv2.imread('testpic.jpeg')

decodedObjects = decode(im)
width = retrieve_width(decodedObjects)
qr_code_length = 5.55 # The size of the QR code on a standard paper is 5.55 cm. 
pixel_size = qr_code_length / width
pixel_dict = {}
pixel_dict['pixel_width'] = pixel_size

result.update(pixel_dict)


with open('points.json', 'w') as json_file:  
    json.dump(result, json_file)













