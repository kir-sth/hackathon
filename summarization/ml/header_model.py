import json
import requests
from dataclasses import dataclass
from typing import Dict, List

from config_reader import config


class Base():
    def __init__(self) -> None:
        self.url = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion"
        self.headers = {
            "Content-Type": "application/json",
            "x-folder-id": "b1gjnrav3hvkeoq9lgdr",
            "Authorization": "Bearer " + config.iam_token.get_secret_value()
        }
    
    def make_data(self, text_arr):
        message = [
            {
                "role": "system",
                "text": "Придумай 3 заголовка для текста. Заголовок должен состоять максимум из 5 слов"
            }
        ]
        for text in text_arr:
            message.append(
                {
                    "role": "user",
                    "text": text
                }
            )
        self.data = {
            "modelUri": "gpt://b1gjnrav3hvkeoq9lgdr/yandexgpt-lite/latest",
            "completionOptions": {
                "stream": False,
                "temperature": "0.6",
                "maxTokens": "2000"
            },
            "messages": message
        }
        return json.dumps(self.data)
    
    def predict(self, text_arr: str) -> Dict[str, str]:
        response = requests.request(
            "POST", 
            self.url, 
            headers=self.headers, 
            data=self.make_data(text_arr)
        )
        jsn = response.json()
        summarized_header = jsn["result"]["alternatives"][0]["message"]["text"]
        summarized_header = summarized_header.split("\n")[0].split(". ")[1].strip().replace(".", "") # postprocessing needed
        return {
            "summarized_header": summarized_header
        }
    
base = Base()

     
@dataclass
class Prediction:
    summarized_header: str


def load_model():
    def model(text_arr: List[str]) -> Prediction:
        prediction = base.predict(text_arr=text_arr)
        return Prediction(
            summarized_header=prediction["summarized_header"],
        )
    return model