import { MyContext } from "#root/bot/context.js"
import {
  type Conversation,
  createConversation,
} from "@grammyjs/conversations";
import { config } from "#root/config.js";

export interface Channel {
  title: string,
  username: string,
  url: string,
}

export function collectChannelsConversation() {
  return createConversation(
    async (conversation: Conversation<MyContext>, ctx: MyContext) => {
      const MAX_ADDED_CHANNELS = Number(config.MAX_ADDED_CHANNELS);
      // let len = 0;
      // const channels = (ctx.session.channels = ctx.session.channels ?? []);
      // let channels = ctx.session.channels = ctx.session.channels || []
      ctx.session.channels ??= []

      await ctx.reply("Перешлите мне сообщения от каналов, на которые нужно подписаться");

      while (ctx.session.channels.length < MAX_ADDED_CHANNELS) {
        ctx = await conversation.wait();

        if (ctx.hasCommand('stopChannel')) {
          console.log(`on EXIT...:
          ${JSON.stringify(ctx.session.channels || 'HUYYYYETA', null, 2)}
          `);
          await ctx.reply('command heard.. exiting');
          return;
        }
        else if (ctx.has(':forward_origin:channel')) {


          //@ts-expect-error
          const forwardOrigin = ctx.message?.forward_origin?.chat || ctx.message?.forward_from_chat;
          const originUrl = `https://t.me/${forwardOrigin.username}`
          const originMsg = `Канал ${forwardOrigin.title} добавлен в список: \n${originUrl}`;

          ctx.session.channels.push({
            title: forwardOrigin.title,
            username: forwardOrigin.username,
            url: originUrl,
          });

          console.log(`on STEP ${ctx.session.channels.length}...:
          ${JSON.stringify(ctx.session.channels || 'HUYYYYETA', null, 2)}
          `);

          await ctx.reply(originMsg, {
            // parse_mode: 'Markdown',
            link_preview_options: {
              is_disabled: false,
              url: originUrl,
              prefer_small_media: true,
              show_above_text: false,
            }
          });
        } else {
          await ctx.reply("Нужно пересланное сообщение от канала");
        }
      } if (ctx.session.channels.length === MAX_ADDED_CHANNELS) {
        await ctx.reply(`В подписку можно добавить не более ${MAX_ADDED_CHANNELS} каналов`);
        return;
      }
    },
    'collectChannelsConversation',
  );
}