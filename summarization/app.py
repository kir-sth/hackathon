import uvicorn
from contextlib import asynccontextmanager
from fastapi import FastAPI
from pydantic import BaseModel
from typing import Dict, List

from ml.text_model import load_model as load_text_model
from ml.header_model import load_model as load_header_model


MODELS = {}

class TextOutput(BaseModel):
    summarized_text: str

class HeaderOutput(BaseModel):
    summarized_header: str

class CardOutput(BaseModel):
    header: str
    text_arr: List[str]


@asynccontextmanager
async def lifespan(app: FastAPI):
    MODELS["text_summarizer"] = load_text_model()
    MODELS["header_summarizer"] = load_header_model()
    yield
    MODELS.clear()

app = FastAPI(lifespan=lifespan)

@app.get("/")
async def root():
    message = "Welcome! Click [here](/docs) to access the API documentation."
    return {"message": message}

@app.post("/get_summarization")
async def get_summarization(data: Dict[str, List[str]]) -> TextOutput:
    model = MODELS["text_summarizer"]
    text_arr = data["text_arr"]
    predict = model(text_arr=text_arr)
    response = TextOutput(
        summarized_text=predict.summarized_text,
    )
    return response

@app.post("/get_header")
async def get_header(data: Dict[str, List[str]]) -> HeaderOutput:
    model = MODELS["header_summarizer"]
    text_arr = data["text_arr"]
    predict = model(text_arr=text_arr)
    response = HeaderOutput(
        summarized_header=predict.summarized_header,
    )
    return response

@app.post("/get_card")
async def get_header(data: Dict[str, List[str]]) -> CardOutput:
    text_arr = data["text_arr"]
    header_predict = MODELS["header_summarizer"](text_arr=text_arr)
    text_predict = MODELS["text_summarizer"](text_arr=text_arr)
    header = header_predict.summarized_header
    new_text_arr = text_predict.summarized_text.replace("- ", "").split("\n")
    response = CardOutput(
        header=header,
        text_arr=new_text_arr,
    )
    return response


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
