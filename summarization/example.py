import requests
from typing import Dict
from config_reader import config


class Base():
    def __init__(self) -> None:
        self.endpoint = 'https://300.ya.ru/api/sharing-url'
        self.headers = {'Authorization': config.summarization_token.get_secret_value()}

    def predict(self, text: str, prompt: str) -> Dict[str, str]:
        response = requests.post(
            self.endpoint,
            json={
                'article_url': 'https://habr.com/ru/news/729422/'
            },
            headers=self.headers
        )
        summarized_text = response.json()
        return {
            "summarized_text": summarized_text
        }
    
model = Base()
print(model.predict(text="qwe", prompt="rty"))