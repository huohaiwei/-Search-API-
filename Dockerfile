# 所基maven:3.8.1-jdk-8-slim镜像的基础创建新的镜像
FROM maven:3.8.1-jdk-8-slim as builder

# 为生成的镜像添加元数据标签信息
#LABEL

# 指定环境变量，在镜像生成过程中会被后续的RUN指令使用
#ENV
#ENV PATH $PATH:/usr/local/bin
# 由于ENV先赋值再更新，以下结果为 key1=value1 key2=value2
#ENV key1=value2
#ENV key1=value1 key2=${key1}

# 配置工作目录.
#WORKDIR
#复制内容到容器中
#COPY

WORKDIR /app
COPY pom.xml .
COPY src ./src

# 运行指定的命令，每运行一条RUN指令，镜像新添一层，并提交
#当命令过长可以用 \ 来换行
RUN mvn package -DskipTests

# 启动容器时指定的默认操作.
CMD ["java","-jar","/app/target/yellowapi-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]