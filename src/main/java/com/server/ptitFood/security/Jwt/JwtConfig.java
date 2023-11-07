package com.server.ptitFood.security.Jwt;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtConfig {

    private String publicKey = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwxOkREu1N9cgGgABR0tB
            H7P5yRIft6np7PZw1JJVkaat9l2/qzZS6jl4tjuBs+pMBK8h96zUi9EfJYCKKKvm
            QEx46mGhH+QwfT1kYJt/gXR6YDOG+uDikFMUzC3HwaNZN8DL1MbmicC1HbWU/LNv
            5RdpzbewefK5I5ZJmykql7/QHBZO0Y1A/2nivvKJteMjUu6KcAtvsyCicCzDG9w5
            9gv1b+Ky82hD1dpUr8/NtYAW3PxJRVpGp0q7C60Lz03/Wm1w8s3rEI0d/NtlD+Ed
            LD+hBt8AmO951p5wE7nC0P0TlxpvASXkaqfBlO87dObqRgd3fLsRdQBQGwlFiN3X
            BQIDAQAB
            -----END PUBLIC KEY-----
            """;
    private String privateKey = """
            -----BEGIN PRIVATE KEY-----
            MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDDE6RES7U31yAa
            AAFHS0Efs/nJEh+3qens9nDUklWRpq32Xb+rNlLqOXi2O4Gz6kwEryH3rNSL0R8l
            gIooq+ZATHjqYaEf5DB9PWRgm3+BdHpgM4b64OKQUxTMLcfBo1k3wMvUxuaJwLUd
            tZT8s2/lF2nNt7B58rkjlkmbKSqXv9AcFk7RjUD/aeK+8om14yNS7opwC2+zIKJw
            LMMb3Dn2C/Vv4rLzaEPV2lSvz821gBbc/ElFWkanSrsLrQvPTf9abXDyzesQjR38
            22UP4R0sP6EG3wCY73nWnnATucLQ/ROXGm8BJeRqp8GU7zt05upGB3d8uxF1AFAb
            CUWI3dcFAgMBAAECggEAIM4TyLxmjC5sIpMvo9qe6ALvFYYruUokoYnL7aNyKl06
            Ufzv943aIbNqflLBfA0BnaQJ9UmmMKEJONhF45MM1HFHwDqMyv0nvlftomVj6tQ4
            pCbmIxhgYV7nV3K3bfPXVA3i+TQ9npq+mnFnzZsrTP5D96lVTxynT7m9RQ+GLyBy
            ZbFbo4cW6fUR/3GKWSyfvs9yI7Oa6F6JfUMwrRxkvoE0OPFvhm0L4GL+7gMBInF5
            tFeVEwsI9udUMSiNlf4V5EugevZkiAbaYDd8a4OxVRsQv9BlwHx3NW4wnU4BruFI
            hQUUzW6zcjMkltEGJX7GD9HDZeefmnM5Jbc5pPdrQQKBgQDjXnQNClR2IfLSp1Ii
            2qfIObE0b22Ki/ue0h5B60Xm0sKRD3LYBoDnFsauFFoo6GmQogK+yrOu5armipDg
            D51II6tN6durATaPWZCbEn95mtlzSCKvIRh+DmKvK7zaTjKWTZm7AmQgWQRZIhSO
            fcy4wFnbZmw8uOsezWZHP0aldQKBgQDbpDRVIU7NfSMpuXgs0LXFc4YclBOURbPr
            Hbov4iqkLxxzln9QfMpR3Y4sVTfsK+pROjmMdlPmSv18Ffajx8gKJ1KZcIZL5YqU
            U8LpUylEZ+1fCdpQ78NK/kzBc8ML79NmWonWA0ZVRZYmRZ1CutOW5nmStejerLuc
            LbBjQbzpUQKBgQDYBxpEeZgeVcsdExUbQJ1BxG713xk5Fh+QmxtcjmnwEjgDE6aO
            Uj+PUTiclYHEdG44mXX6SSXH+zcRrH5SJQPPUvSkJposNA7ezL50WHL9237X5aXu
            t4dJ3ektfmUFFl9CJ6D06iSwB2P2PEdDy4sDzt6RcxNL7naW4ZlYAGrjMQKBgG9p
            NG52Lb3IGv0AKjg5Seq1NbLrXZ8ZWDEL7DR7uXt8tibfkQbntcye4I4WT/6JLBs1
            uJlKMDahlUDK90eOgCa+WYShck2vSoMAld66ozUirmJF4iqp49r3cUGzkZ1NcmL5
            y+71zEhWutoA98swb0Gc+M/1XAEPhz6qYk2Gu+dxAoGBAId5jIpxh0oxGdhwNkb7
            hdhEGYQuIwsRAEtqgfTrzjB2cd2pYtmwnG7dnOH5ne6djRJgFqpIRj/4ag6lOrAC
            ikWS+PuGwNulEB9BtzkmuB0P5S5WrB9AqcmYu0dUMagBqwwKYUajHZBTuYGg50tb
            v1SgHmrpP6vkqbjIBSUMQh4u
            -----END PRIVATE KEY-----
            """;
}