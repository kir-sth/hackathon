import { Context, SessionFlavor } from "grammy";
import {
    type Conversation,
    type ConversationFlavor,
  } from "@grammyjs/conversations";
import { type Channel } from "#root/bot/conversations/collectChannels.js";

export interface SessionData{
  channels: Channel[]
}
export type MyContext = Context & ConversationFlavor & SessionFlavor<SessionData> ;
export type MyConversation = Conversation<MyContext>;