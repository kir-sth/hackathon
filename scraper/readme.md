### Описание
Сервис сбора постов телеграм-каналов

### Регистрация в реестре контейнеров
- `yc init` – настроить профиль для папки облака, например `aaakl6ccccsrooooojt9`
- Создать реестр контейнеров, получить его идентификатор – `crrrr7tqttttc0ssss3i`
- Получить OAuth токен здесь https://oauth.yandex.ru/authorize?response_type=token&client_id=1a6990aa636648e9b2ef855fa7bec2fb
- Залогиниться `docker login --username oauth --password y0_AgAAAAA***********************DQ cr.yandex`

### CI/CD
- `cd hackathon/scraper`
- `docker build -t scraper . --progress plain`
- `docker tag scraper:latest cr.yandex/crrrr7tqttttc0ssss3i/scraper:latest`
- `docker push cr.yandex/crrrr7tqttttc0ssss3i/scraper:latest`
- создать новую ревизию в Serverless Containers
