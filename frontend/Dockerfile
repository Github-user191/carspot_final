FROM node:14.21.1-alpine3.17 as BUILD

RUN mkdir -p /app
RUN chown node /app
USER node
WORKDIR /app

COPY --chown=node package.json package-lock.json ./

RUN npm install

COPY --chown=node . .
RUN npm run build

FROM nginx:1.21.6-alpine
WORKDIR /etc/nginx/conf.d
COPY --from=BUILD /app/build/ /usr/share/nginx/html
COPY ./default.conf ./default.conf
#CMD ["npm", "start"]
ENTRYPOINT  ["nginx", "-g", "daemon off;"]
