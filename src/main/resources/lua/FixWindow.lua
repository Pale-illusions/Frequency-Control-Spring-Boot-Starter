---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by cangjingyue.
--- DateTime: 2024/11/8 23:32
---

local key, ttl = KEYS[1], ARGV[1]

if redis.call('EXISTS', key) == 0 then
    redis.call('SETEX', key, ttl, 1)
    return 1
else
    return tonumber(redis.call('INCR', key))
end