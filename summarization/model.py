import json
import requests
from dataclasses import dataclass
from typing import Dict

from config_reader import config


class Base():
    def __init__(self) -> None:
        self.url = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion"
        self.headers = {
            "Content-Type": "application/json",
            "x-folder-id": "b1gjnrav3hvkeoq9lgdr",
            "Authorization": "Bearer " + config.iam_token.get_secret_value()
        }
    
    def make_data(self, text):
        self.data = {
            "modelUri": "gpt://b1gjnrav3hvkeoq9lgdr/summarization/latest",
            "completionOptions": {
                "stream": False,
                "temperature": "0.6",
                "maxTokens": "2000"
            },
            "messages": [
                {
                "role": "system",
                "text": "Найди ошибки в тексте и исправь их"
                },
                {
                "role": "user",
                "text": text
                }
            ]
        }
        return json.dumps(self.data)
    
    def predict(self, text: str) -> Dict[str, str]:
        response = requests.request(
            "POST", 
            self.url, 
            headers=self.headers, 
            data=self.make_data(text)
        )
        jsn = response.json()
        summarized_text = jsn["result"]["alternatives"][0]["message"]["text"]
        return {
            "summarized_text": summarized_text
        }
    
base = Base()

     
@dataclass
class Prediction:
    summarized_text: str


def load_model():
    def model(text: str) -> Prediction:
        prediction = base.predict(text=text)
        return Prediction(
            summarized_text=prediction["summarized_text"],
        )
    return model