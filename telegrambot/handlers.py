from aiogram import Bot, F, Router
from aiogram.filters import Command
from aiogram.types import (
    InlineKeyboardButton,
    InlineKeyboardMarkup,
    MenuButtonWebApp,
    Message,
    WebAppInfo,
)

router = Router()


@router.message(Command("start"))
async def command_start(message: Message, bot: Bot, base_url: str):
    await message.answer("""Hi!\nSend me any type of message to start.\nOr just send /webview""")


@router.message(Command("webview"))
async def command_webview(message: Message, base_url: str):
    await message.answer(
        "Good. Now you can try to send it via Webview",
        reply_markup=InlineKeyboardMarkup(
            inline_keyboard=[
                [
                    InlineKeyboardButton(
                        text="Open Webview", web_app=WebAppInfo(url=f"{base_url}")
                    )
                ]
            ]
        ),
    )


@router.message(~F.message.via_bot)  # Echo to all messages except messages via bot
async def echo_all(message: Message, base_url: str):
    await message.answer(
        "Test webview",
        reply_markup=InlineKeyboardMarkup(
            inline_keyboard=[
                [InlineKeyboardButton(text="Open", web_app=WebAppInfo(url=f"{base_url}/demo"))]
            ]
        ),
    )