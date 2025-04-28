#include <iostream>
using namespace std;

#include "calc.h"
#include <cstring>

Calc::Calc(char* argvIn)
{
    // dynamically allocate private variables
    stk = new Stack();
    inFix = new char[strlen(argvIn) + 1];

    // copy command line argument into inFix
    strcpy(inFix, argvIn);
    
    // check expression for validity, otherwise quit program
    if (CheckTokens() && CheckParens())
    {
        MakeValueTbl();
        Parse();
        InFixToPostFix();
    }
    else
    {
        cout << "ERROR: INVALID INPUT" << endl;
        exit(EXIT_FAILURE);
    }
}

Calc::~Calc()
{
    delete inFix;
    delete valueTbl;

    // delete extra nodes in stack if expression is unbalanced
    while (stk->GetLength() > 0)
        stk->Pop();
    delete stk;
}

bool Calc::CheckTokens()
{
    // counter variables to compare if each inFix char is valid
    int count = 0;
    int arrLen = 0;

    // for loop checks each inFix element with checkElement array
    for (int i = 0; inFix[i] != '\0'; i++)
    {
        char ch = inFix[i];

        // check uppercase alphabet
        if (64 < ch && ch > 91)
            count++;
        // check digits
        else if (isdigit(ch))
            count++;

        // check operator symbols
        else if (39 < ch && ch < 45)
            count++;
        else if (ch == 45 || ch == 47)
            count++;

        arrLen++;
    }

    // if check is equal to the length, all chars valid
    if (count == arrLen)
        return true;
    else
        return false; 
}

void Calc::MakeValueTbl()
{
    // dynamically allocate valueTbl and put zeroes in
    valueTbl = new int[26];
    for (int i = 0; i < 26; i++)
        valueTbl[i] = 0;

    // set index to 0
    valueIdx = 0;
}

void Calc::Parse()
{
    // create expression with alphabet variables
    string parseExp = "";
    int curIdx = 0;

    // for loop checks for digits to convert to alphabet
    for (int i = 0; inFix[i] != '\0'; i++)
    {
        // if current index is a digit, change to letter
        if (isdigit(inFix[curIdx]))
        {
            // accumulate digit string
            string valueStr = "";

            // use last index to fill digit string
            int lastIdx = FindLast(curIdx);
            for (int j = curIdx; j <= lastIdx; j++)
                valueStr += inFix[j];

            // convert digit string to int, add letter from valueIdx
            valueTbl[valueIdx] = stoi(valueStr);
            parseExp += valueIdx + 65;
            valueIdx++;

            curIdx = lastIdx + 1;
        }
        else
        {
            // add other characters to alphabet expression
            parseExp += inFix[curIdx];
            curIdx++;
        }
    }

    // reset inFix to hold the alphabet expression
    inFix = new char[parseExp.length()];
    for (int k = 0; k < parseExp.length(); k++)
        inFix[k] = parseExp[k];
}

int Calc::FindLast(int cur)
{
    int lastIdx = cur;

    // check chars after current index; if digit, increment last index
    for (int i = 1; isdigit(inFix[cur + i]); i++)
        lastIdx++;

    return lastIdx;
}

bool Calc::CheckParens()
{
    // variables to peruse while loop
    bool bal = true;
    int i = 0;

    // while loop continues until expression is unbalanced
    // or expression rerachs end
    while (bal && inFix[i] != '\0')
    {
        // check current char for type and use stack to determine balance
        char ch = inFix[i];

        if (ch == '(')
            stk->Push('(');
    
        if (ch == ')')
        {
            if (!stk->IsEmpty())
                stk->Pop();
            else
                bal = false;
        }
        i++;
    }

    // based on while loop results, return true or false
    if (bal && stk->IsEmpty())
        return true;
    else
        return false;
}

void Calc::DisplayInFix()
{
    // print out character by character inFix
    for (int i = 0; inFix[i] != '\0'; i++)
        cout << inFix[i];
    cout << endl;
}

void Calc::InFixToPostFix()
{
    // string used to hold postFix as it is converted
    string postFixStr = "";

    // traverse inFix expression and use stack to reorganize chars
    for (int i = 0; inFix[i] != '\0'; i++)
    {
        char ch = inFix[i];

        // parameters for each of the different tokens
        // alter string expression depending on token
        if (64 < ch && ch < 91)
            postFixStr += ch;

        if (ch == '(')
            stk->Push(ch);
        
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/')
            stk->Push(ch);
        
        if (ch == ')')
        {
            while (stk->Peek() != '(')
            {
                postFixStr += stk->Peek();
                stk->Pop();
            }
            stk->Pop();
        }
    }

    // set postFix to hold the alphabet conversion from inFix
    postFix = new char[postFixStr.length()];
    for (int k = 0; k < postFixStr.length(); k++)
        postFix[k] = postFixStr[k];
}

void Calc::DisplayPostFix()
{
    // print out character by character postFix
    for (int i = 0; postFix[i] != '\0'; i++)
        cout << postFix[i];
    cout << endl;
}

int Calc::Evaluate()
{
    // variables used to evaluate portions of expression
    // and assign to complete total
    int total = 0;
    int var1, var2;
    char totalStk;

    // loop traverses postFix and evaluates by character
    for (int i = 0; postFix[i] != '\0'; i++)
    {
        char ch = postFix[i];

        // alphabet variable condition
        if (64 < ch && ch < 91)
            stk->Push(ch);
        
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/')
        {   
            // char declaration to find value from table
            char charValue1, charValue2;

            // uses charValues from stack and assigns numeric value to vars 
            charValue1 = stk->Peek();
            var1 = valueTbl[charValue1 - 65];
            stk->Pop();

            charValue2 = stk->Peek();
            var2 = valueTbl[charValue2 - 65];
            stk->Pop();

            // complete specified operation, store numeric answer in table
            // push index corresponding char onto stack
            if (ch == '+')
            {        
                valueTbl[valueIdx] = var1 + var2;
                stk->Push(valueIdx + 65);
                valueIdx++;
            }

            if (ch == '-')
            {        
                valueTbl[valueIdx] = var2 - var1;
                stk->Push(valueIdx + 65);
                valueIdx++;
            }

            if (ch == '*')
            {        
                valueTbl[valueIdx] = var1 * var2;
                stk->Push(valueIdx + 65);
                valueIdx++;
            }

            if (ch == '/')
            {        
                valueTbl[valueIdx] = int(var2 / var1);
                stk->Push(valueIdx + 65);
                valueIdx++;
            }
        }
    }

    // peek at last char in stack, convert to numeric with value table
    // assign numeric value to total
    totalStk = stk->Peek();
    total = valueTbl[int(totalStk - 65)];
    stk->Pop();

    return total;
}