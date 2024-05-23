colima stop && \
colima delete -f && \
colima start && \
docker buildx build --platform linux/amd64 -t scraper:latest . --load && \
docker tag scraper:latest cr.yandex/$1/scraper:latest && docker push cr.yandex/$1/scraper:latest && \
colima stop
