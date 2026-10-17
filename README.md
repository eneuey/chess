# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAHZM9qBACu2AMQALADMABwATACcIDD+yPYAFmA6CD6GAEoo9kiqFnJIEGiYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAEQDlGjAALYo43XjMOMANCu46gDu0ByLy2srKLPASAj7KwC+mMK1MJWs7FyUDRNTUDPzF4fjm6o7UD2SxW63Gx1O52B42ubE43FgD1uogaUCyOTAlAAFJlsrlKJkAI5pXIASkwOlRwAA1jAAELADgwQnErCw54IxE1F50hnMtRgACiAA8VNgCIUblyOVUSuY6oEnE4RhN5upgPYFksBVBvPUeYy+bloWZOJKRCp7lVZPIlCp1A0NWAAKqDTHvT4oMnWxTKNSqS1GHR1ABiSE4MBdlG9MB0lhg7rmYgpKGpEcGMC2SDACXjgw9MGACEpHDjKCF6I0xnuD29tr9DUjUG9mFrvvU1aqSJUDcGza7hjKnOo3IURZTJY61PQwtF4qKt0oHfgyDlMAVwWV41VqnVmpW2t1DQT8wLY4ZcfkVPQxvQHE0OiXrbtqgaaB8CAQLe0dfbg87NWRGA3w-M0qFEAMn3rEBKQxRs3V7bQvW-Nt-QeYw6gUDhGUbPsAItGtkOfOpoJTDEFB8bNMWACiEm9JCbRQgN0MwxlyOzXDzQHB42XhBocXRfE1HfVknnhJcFz1N480TZYGgOUFqOzDoICvNBZJWK5QMXP9l1KMAGnCRVN2PBYYDkkEVkUhJlNU9SDmuW9TC8XwAmgdgNViYM4AFaQ4AUGAABkIGyIpZXKf9hz1Fp2m6PoDHUAo0GVEz1j+AEOGuCSAx4l5RhSmA0t2a5k1TekDRQIl+UwHLpWqSKGjKw1BRFFAxUSrTarC-SFSVMYtz9Xd9gPaAGoZJkKpZG9TRqgN+zqBBgrDTEgpCpqyX7CDCPrGAQASFAQCpOBUQ4cwkELVR4OmRNUv0f5dnon1nyYoNQ3DHDtBjOMTJgAAyArbvS8lKRpMr4GO8xzpge9Hy2+0gPfT9IN-IcwO7eGQI2nSJIaABJNAqELJAOCOlATowM6EFUGdWrnDqly6+UnAARk3bdBrk4a9R8GYaOgJAAC9Samu8qx0pGXzTKNtC-BinqxvCUB7KX5FA8CCNlqCYJQNiEiomi6Jlx6-WejCsJgHWONRriqhqhoVrDTJVGE6rRO0lHuSkq6vjMjSfismzrx9+y6Z0hmYEM3rPY+GSg4s8Z-ZUwPzOFpzvD8fwvBQdBYniJJM+zlbfCwLrZqlBpGmkAUAoFDoBR6Xp4tURKRgT1SQ5t136gmVvryhYrgf1cbKtyF24TdiLalGjgmuptqJSy0OVwwRnI-6tUNSGnURsHpqU5mlHkQW+wi+W4Ki7W1X8KtWGJYJ5AtDImiA7QPWlMTtAHp-VCqnQuA9oOmASAABmBYn7v0Af6O+RMgYphBmNEmZN5CUyhqLdWRs4bAURjfcSCtXwI0vtbOqk8YB4ygcTcG5Nzqz1pgvB4DMFQsz6mzDeHMt5cx5tmPmgs9gOVNKg6+Gs4bvRVuLHBnElZNmlpjAR6CXwnW4I-SiVkDaiLQkGaQKAFGGAtlIhWS5baBTPhRR2zt97u0kplKU9Ml76XDkZMYvC7yeDTgEY6sRsBhhpAFdEMAADiiYNAl3MeXXxNd672ETC3MBbcF4dzHl3eO0Te7BxKnA8qw8RLxLEcQxqE1+TUParQmUNiV6swGiw8YnMp5D0mo40e7JS7iOQLkfxqpMTNLAK0tQF9pEyBvg0MhchtZJJfj3D+hsv4mz-vtGkwDQFv1UhAmAZCYGlXgRQpB-poZi36ejLBgjUIHzRpggh2TuSkMJuQ0mp0qEtTnvOKxi89KM0YSqcpe5KlsKPBwhIXChZ1P4X0g5EjmyqKOYrSWkiVa9PFnUR0XSLqf0YmouoviUDMARdstBX8HSJm8SPMFE9AIRPmPirAmNuKdztuiBFJiECZIafLeqMAJgkrUIsRoXRLGRWsc8uxq82WqA5Vyk0TjnLpw4G4SITgUBOFiAKYIcBvIADYwakUMF0qGQSJ7cmip0cJkSfnP2VGygAcombldxKXxLqN3EZ3xVjjDNTJPuqy0k1KqmYnVepckZIKfPR5dCSk9TKevTUnzDw7zyUaOp+8iXdhIkMrpmISZJsTD0vR2KUIDMuUMnWz9X7WXfkiuWP8gzTIAXMsZSyVmpMHggiGyCsUyJxXsiZyLwV4Ixpm4JJD8aXIbZQym-qHk8qeXKBhoadwsP3F8+MPy-k8NFc2oFsiQXS0JUQwCwjgCnKzURRNGJk3OvmCW42KLU0Yj8YmFdsKYBspxtIdtctO33sTI+vd1r2QNEvSgLpdKGViSZZPUYKwH3SEWDAIUMBLXj10quCOyowPvogw0aDdTnEuX8JYTRC0tg5yQIkMAOGPwQHwwAKQgGGa9J5-ApFAFSYoNjGnEJaE6WKvQ2VRIWegZU2AEDABw1AOAEAFpQHWOB2DCI4nfrtTxtSUJHX8cE5QETYmHVOpQ98fusCo0ZPqUB3tvqWQjpDkG55IamHvM3pG0Gu9Y2dxY8iAAVlRtAybKNhj-em6NYB1qZpbdm5ZubFFFtUoW5+Z7fxlrqBW2ZIDq05GC-fN19aNmQ1vbsk5m65ptopd63G-b76DoILc2chTA3FIs8zKd7NZ2Ru5lZRdKdAV3p3c+89r72swv6fCxMmJwNRe-oGOF6KaNJgHpqjgEA1BoAAOTMDLOiTLwK205dwXlntBX9QzzuTQyr8Hl6WbeWGmz287O+Zaw+HZq2ukdeRvGiFd262aqAfg-LRDuTBgRl00zRTDvdScBuMYKxmEfKqaeYsF4pxqRg8uj7BjPPuZ807elBm4PY1A78ATQm1PQAdch+Yj7IPobM1VhD9isfKdx6J-HinCcoGJ2huHjlMPpy8IJgjRGOdxkQCmWAwBsD8cIPkQoWrmO9orlXGudc+jGHbo8G1owdNrPSSydHtVsY7d839g79DKdr2neGiHF2Ml70c52kA3A8CYn85xTaq2rf89t-d4bv9rewGkLSUwCOqU7Q9wBjXZyu5Sd5RTyOdSgA
