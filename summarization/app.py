import uvicorn
from contextlib import asynccontextmanager
from fastapi import FastAPI
from pydantic import BaseModel
from typing import Dict

from model import load_model


MODELS = {}

class OutputData(BaseModel):
    text: str
    summarized_text: str


@asynccontextmanager
async def lifespan(app: FastAPI):
    MODELS["summarizer"] = load_model()
    yield
    MODELS.clear()

app = FastAPI(lifespan=lifespan)

@app.get("/")
async def root():
    message = "Welcome! Click [here](/docs) to access the API documentation."
    return {"message": message}

@app.post("/predict")
async def predict_recomendation(data: Dict[str, str]) -> OutputData:
    model = MODELS["summarizer"]
    text = data["text"]
    predict = model(text=text)
    response = OutputData(
        text=text,
        summarized_text=predict.summarized_text,
    )
    return response

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)