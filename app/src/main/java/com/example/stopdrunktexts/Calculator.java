package com.example.stopdrunktexts;

public class Calculator
{
    //random code from: https://www.javatpoint.com/nth-prime-number-java
    public static int nthPrime(int n){
        int num=1, count=0, i;
        while (count < n)
        {
            num=num+1;
            for (i = 2; i <= num; i++)
            {
                if (num % i == 0)
                {
                    break;
                }
            }
            if (i == num)
            {
                count = count+1;
            }
        }
        return num;
    }


    //random code from: https://www.geeksforgeeks.org/nearest-prime-less-given-number-n/
    static int nearestPrime(int n)
    {
        if (n % 2 != 0)
            n -= 2;
        else
            n--;
        int i, j;
        for (i = n; i >= 2; i -= 2) {
            if (i % 2 == 0)
                continue;
            for (j = 3; j <= Math.sqrt(i); j += 2) {
                if (i % j == 0)
                    break;
            }
            if (j > Math.sqrt(i))
                return i;
        }
        return 2;
    }
}
