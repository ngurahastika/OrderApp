package com.example.orderapp.utils;

public class SnowflakeIdGenerator {

 
    private final Long twepoch = 1704067200000L;


    private final Long workerIdBits = 5L;
    private final Long datacenterIdBits = 5L;
    private final Long sequenceBits = 12L;

    private final Long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final Long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    private final Long workerIdShift = sequenceBits;
    private final Long datacenterIdShift = sequenceBits + workerIdBits;
    private final Long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final Long sequenceMask = -1L ^ (-1L << sequenceBits);

    private Long workerId;
    private Long datacenterId;
    private Long sequence = 0L;
    private Long lastTimestamp = -1L;

    public SnowflakeIdGenerator(Long workerId, Long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("Datacenter ID can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        // Cek jika clock mundur (masalah NTP)
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        // Jika generate di waktu yang sama, naikkan sequence
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // Proses penggabungan bit (Bitwise OR)
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
    
   
}
