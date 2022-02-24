# See chapter "3: Set the Database Connection String" for Sample class
https://blogs.oracle.com/developers/post/ssl-connection-to-oracle-db-using-jdbc-tlsv12-jks-or-oracle-wallets-122-and-lower

# 13.1.7 Choosing Between Native Network Encryption and Transport Layer Security
https://docs.oracle.com/en/database/oracle/oracle-database/12.2/dbseg/configuring-network-data-encryption-and-integrity.html#GUID-8F489523-9693-4147-ADFD-C05246C98790

# How do I configure network encryption
https://confluence.xxx/pages/viewpage.action?spaceKey=ODB&title=How+do+I+configure+network+encryption


# DBaaS Encryption - Checking encrypted/non-encrypted connections
https://confluence.xxx/pages/viewpage.action?pageId=1056397944

We can check if Mobile Payments applications are creating encrypted connections to DBaaS.

For example, if you need to check if DBaaS connections created by applications running on server xxxxxx are encrypted or not you can execute the following SQL command:


```roomsql
SELECT sid, network_service_banner
FROM v$session_connect_info
WHERE sid IN (SELECT s.sid FROM v$session s where s.machine = 'xxxxxx')
ORDER BY sid, network_service_banner;
```

```roomsql
SELECT i.sid, network_service_banner, machine
FROM v$session_connect_info i
JOIN v$session s ON i.sid = s.sid
WHERE s.machine='xxxxxx'
AND i.network_service_banner LIKE 'AES%';
```

And this can be the result of this query:

```
SID   NETWORK_SERVICE_BANNER
---   ----------------------
21    AES256 Encryption service adapter for Linux: Version 19.0.0.0.0 - Production
21    Crypto-checksumming service for Linux: Version 19.0.0.0.0 - Production
21    Encryption service for Linux: Version 19.0.0.0.0 - Production
21    TCP/IP NT Protocol Adapter for Linux: Version 19.0.0.0.0 - Production
59    Crypto-checksumming service for Linux: Version 19.0.0.0.0 - Production
59    Encryption service for Linux: Version 19.0.0.0.0 - Production
59    TCP/IP NT Protocol Adapter for Linux: Version 19.0.0.0.0 - Production
```

If you see the line starting with "AES256 Encryption", then that connection is encrypted, otherwise, it's not.

In the above sample, we have two connections (sessions) identified by SID: 21 and 59.

The connection with SID 21 is encrypted while the connection with SID 59 is not.


# Count the number of DBAAS connections
netstat -anp | grep 1521 | grep ESTABLISHED | grep 10.198.252.14 | wc -l


# Update .tra files for Administrator and Hawk agent
ps -ef | grep tibco

/opt/tibco/administrator/domain/SRV001/bin/tibcoadmin_SRV001.tra
/opt/tibco/tra/domain/SRV001/hawkagent_SRV001.tra

Add the following properties:
java.property.oracle.net.encryption_client=REQUIRED
java.property.oracle.net.encryption_types_client=AES256
