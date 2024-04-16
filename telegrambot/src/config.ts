import "dotenv/config";

const createConfigFromEnvironment = (environment: NodeJS.ProcessEnv) => {

    const isDev = process.env.NODE_ENV === "development";
    const isProd = process.env.NODE_ENV === "production";

    const config = {
        BOT_TOKEN: environment.BOT_TOKEN || '',
        BOT_WEBHOOK: environment.BOT_WEBHOOK || '',
        BOT_SERVER_HOST: environment.BOT_SERVER_HOST || '',
        BOT_SERVER_PORT: environment.BOT_SERVER_PORT || '',
        BOT_ADMINS: environment.BOT_ADMINS || '',
        WEBAPP_URL: environment.WEBAPP_URL || '',
        // BOT_MODE: environment.BOT_MODE || '',
        // BOT_MODE: isDev ? 'polling' as const : 'webhook' as const,
        // BOT_MODE: 'polling',
        BOT_MODE: environment.BOT_MODE || '',
        MAX_ADDED_CHANNELS: environment.MAX_ADDED_CHANNELS || 3,
    }

    return {
        ...config,
        isDev: isDev,
        isProd: isProd
    };
}

export type Config = ReturnType<typeof createConfigFromEnvironment>;

export const config = createConfigFromEnvironment(process.env);