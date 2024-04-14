import requests
import json

with open("mock_data.json", "r") as f:
    mock_data = json.load(f)

for items in mock_data.values():
    data = {
        "text_arr": list(items)
    }
    resp = requests.post(url="http://0.0.0.0:8000/get_card", json=data)
    print(resp.json())
    print(data)
    break