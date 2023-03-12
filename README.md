# OCR Recorder
This is a program designed to take a series of input images, segment them into sections as defined by the config, use OCR to extract numerical data from the segments, and output the results to a CSV file.

This is a hacked together disaster and adheres to no best practices. It should be used for educational purposes only.

Library credits:
- Tesseract-OCR: https://github.com/tesseract-ocr/tesseract
- Tess4J: https://github.com/nguyenq/tess4j
- Seven Segment Display training data: https://github.com/Shreeshrii/tessdata_ssd

Example output image:
![](https://i.imgur.com/8ew7NNR.jpg)

Example output CSV:
```csv
timestamp,image,temperature,voltage
1678580852891,img.00003.jpg,38.7,4.775
1678580872976,img.00004.jpg,39.3,4.775
1678580893048,img.00005.jpg,39.1,4.774
1678580912933,img.00006.jpg,38.8,4.774
1678580952890,img.00008.jpg,39.1,4.773
1678580972966,img.00009.jpg,39.1,4.771
```
The timestamp and image fields are included by default, all other fields are configurable.

# Usage
Run the jar and adjust the config to match what data you are looking to record.

The included Dockerfile is for use with a webcam and it's sole purpose is to output images every 10 seconds. It does not run this program.  