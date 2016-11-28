FROM reminisceme/scala-base:latest

ENV REMINISCEME_FOLDER /home/stats_user/reminisce.me
ENV MONGODB_HOST mongo
ENV STATS_HOST 0.0.0.0

RUN groupadd -r stats_group && useradd -r -g stats_group stats_user

RUN mkdir -p $REMINISCEME_FOLDER
WORKDIR $REMINISCEME_FOLDER

COPY . stats/
RUN cd stats && sbt update && sbt assembly
RUN cp stats/target/scala-2.11/stats.jar .
RUN chown -R stats_user:stats_group .
RUN rm -rf stats/
EXPOSE 7777
USER stats_user

CMD java -Xmx128m -jar stats.jar
