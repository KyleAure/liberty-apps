# OpenLiberty
FROM open-liberty:19.0.0.12-full-java8-openj9

#Copy config
COPY --chown=1001:0 src/main/liberty/config/ /config/

#Copy generated config
COPY --chown=1001:0 build/wlp/usr/servers/defaultServer/configDropins /config/configDropins

#Copy App
COPY --chown=1001:0 build/libs/module-jdbc-direct-lookup-1.0.war /config/apps/

#Install ifixes and apps
RUN configure.sh