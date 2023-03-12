FROM alpine

RUN apk add --no-cache ffmpeg
WORKDIR /app/images

CMD ffmpeg -f v4l2 -framerate 5 -video_size 1920x1080 -i /dev/video0 -r 0.05 -f image2 img.%05d.jpg
