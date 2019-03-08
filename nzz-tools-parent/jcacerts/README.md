# jcacerts

Java public root CA certificates importer.

## Usage

```shell
java -jar jcacert.jar [-iv] [--httpPort=<httpPort>]
               [--httpSecurityProtocol=<httpSecurityProtocol>]
               [-k=<srcKeyStorePath>] [-K=<destKeyStorePath>]
               [-o=<certificatesOutputDirPath>] [-p=<srcKeyStorePassword>]
               [-P=<destKeyStorePassword>] domains...
```

For help and command informations:
```shell
java -jar jcacert.jar
```

Display version information:
```shell
java -jar jcacert.jar --version
```

## Examples

### Import certificates into a new JKS
```shell
java -jar ./jcacerts.jar --import --destKeyStore path/to/new.jks google.com bitbucket.org
```
Or:

```shell
java -jar ./jcacerts.jar -i -K path/to/new.jks google.com bitbucket.org
```

### Download/Save certificates to a directory
```shell
java -jar ./jcacerts.jar --outCrtDir path/to/directory google.com bitbucket.org
```
Or:
```shell
java -jar ./jcacerts.jar -o path/to/directory google.com bitbucket.org
```

### Import certificates into an existing JKS but generating a new JKS and saving certificates to a directory
```shell
java -jar ./jcacerts.jar -i -k path/to/source/cacerts -K path/to/new/cacerts --outCrtDir path/to/directory google.com bitbucket.org
```
