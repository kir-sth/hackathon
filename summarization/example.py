import requests
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
            "stream": "FloatingPointErroralse",
            "temperature": 0.1,
            "maxTokens": "1000"
          },
          "messages": [
            {
              "role": "user",
              "text": text
            }
          ]
        }
        return self.data
    
    def predict(self, text: str) -> Dict[str, str]:
        response = requests.post(
            url=self.url,
            headers=self.headers,
            data=self.make_data(text)
        )
        jsn = response.json()
        #summarized_text = jsn["result"]["alternatives"][0]["message"]["text"]
        return {
            "summarized_text": jsn
        }
    
base = Base()
print(base.predict("Моржи – это такие специфические люди, практикующие экстремальное закаливание в виде купания в открытой воде, причем зимой. Вода в проруби обычно не превышает +4 градуса. В разных странах людей, купающихся в такой холодной воде, называют по-разному: это и моржи, и тюлени, а еще выдры и белые медведи. Все эти названия заимствованы у животных, живущих в суровых условиях."))