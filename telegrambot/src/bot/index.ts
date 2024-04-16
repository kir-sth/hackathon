import { Bot as TelegramBot, session, StorageAdapter } from "grammy";
import { Conversation, conversations, createConversation } from "@grammyjs/conversations";
import { Channel, collectChannelsConversation } from "#root/bot/conversations/collectChannels.js"
import { MyContext } from "#root/bot/context.js";
import { freeStorage } from "@grammyjs/storage-free";
import { type SessionData } from "#root/bot/context.js";


// type Options = {
//     sessionStorage?: StorageAdapter<SessionData>;
//     config?: Omit<BotConfig<Context>, "ContextConstructor">;
//   };

// export function createBot(token: string, options: Options = {})
export function createBot(token: string) {

    const bot = new TelegramBot<MyContext>(token);

    bot.use(session(
        {
            initial: () => (
                {
                    channels: [] as Channel[]
                }
            ),
            // storage: freeStorage<SessionData>(token),
        }
    ));

    bot.command('start', (ctx) => ctx.reply('Приветствую!'));
    bot.command('echo', (ctx) => ctx.reply(ctx.message?.text || 'no message'));
    bot.command('list',
        (ctx) => ctx.reply(
            `\`\`\`json\n${JSON.stringify(ctx.session.channels || ['emptyList'], null, 2)}\n\`\`\``
            || 'empty list of channels...',
            { parse_mode: 'Markdown' }
        )
    );

    bot.use(conversations());
    bot.use(collectChannelsConversation());
    bot.command('convo', async (ctx) => {
        console.log(`SESSION CONTENT:
      ${JSON.stringify(ctx.session.channels, null, 2)}
      `);
        await ctx.conversation.enter("collectChannelsConversation");
    })

    return bot;
}

export type Bot = ReturnType<typeof createBot>;
