import json
import uvicorn
import requests
from fastapi import FastAPI
from pydantic import BaseModel
from typing import Dict, List

app = FastAPI()

# mock data
with open("mock_data.json", "r") as f:
    mock_data = json.load(f)


@app.get("/")
async def root():
    message = "Welcome! Click [here](/docs) to access the API documentation."
    return {"message": message}

@app.get("/get_cards")
async def get_cards():
    cards = []
    for items in mock_data.values():
        jsn = {
            "text_arr": list(items)
        }
        resp = requests.post(url="http://0.0.0.0:8000/get_card", json=jsn)
        if resp.status_code == 200:
            card = resp.json()
            cards.append(card)
    return cards

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=4000)