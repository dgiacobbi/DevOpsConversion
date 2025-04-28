/*
Name: David Giacobbi
Class: CPSC 122, Section 1
Date Submitted: March 2, 2022
Assignment: Project 7
Description: This program creates the functions for string class.
*/

#include <iostream>
using namespace std;

#include "MyString.h"
#include <cstring>

//Constructor
MyString::MyString(char const* strIn)
{
    length = strlen(strIn);  //strlen function
    str = new char[length + 1];
    strcpy(str, strIn); //strlen function

}

//Destructor function
//once you get the constructor working, remove the comments.
MyString::~MyString()
{
    cout << "OMG, I'm about to be deleted" << endl;
    delete []str; 
}

void MyString::myDisplay()
{
    cout << str << endl;
}

void MyString::myStrcpy(char const* strIn)
{
    int strInLen = 0;
    // Traverse through string, assigning the input to str
    for(int i = 0; strIn[i] != '\0'; i++)
    {
        str[i] = strIn[i];
        strInLen++;
    }
    
    // Add null terminator to end so strlen can be found
    str[strInLen + 1] = '\0';

    // Update length of str
    length = strInLen + 1;
}

int MyString::myStrlen()
{
    int count = 0;

    // Increment count everytime loop finds a character before
    // the null terminator
    for(int i = 0; str[i] != '\0'; i++)
        count++;
    
    return count;
}

bool MyString::isEqual(char const* strIn)
{
    bool equalString = true;
    int compareLen = 0;

    // Find the length of string being compared
    for(int i = 0; strIn[i] != '\0'; i++)
        compareLen++;

    // If-statement determines if strings are equal length
    if(length == compareLen)
    {
        // If equal length, check each char value to make
        // sure they are the same also
        for(int i = 0; str[i] != '\0'; i++)
        {
            // If not the same, return false
            if(str[i] != strIn[i])
            {
                equalString = false;
                break;
            }
        }
    }
    else
    {
        // Return false if not the same length
        equalString = false;
    }
    return equalString;
}

int MyString::find(char const* strIn)
{
    // Index starts here until substring
    // can be proven to be found
    int index = -1;

    // Traverses string to check if any characters
    // in str match with the start of substring
    for(int i = 0; i < length; i++)
    {
        if(strIn[0] == str[i])
        {
            // If match is found, use isSub function checks
            // to see if rest of substring is a match
            if(isSub(strIn, i + 1))
                // Return starting index if true
                index = i;          
        }
    }
    return index;
}

void MyString::concat(char const* strIn)
{
    int strInLen = 0;
    // For loop traverses through input string and adds
    // by character to the end of str
    for(int i = 0; strIn[i] != '\0'; i++)
    {
        str[length + i] = strIn[i];
        strInLen++;
    }
    
    // Update length of str
    length = length + strInLen;
}

bool MyString::isSub(char const* strIn, int idx)
{
    bool substring = true;
    int strInLen = 0;

    // For loop determines length of substring
    for(int i = 0; strIn[i] != '\0'; i++)
        strInLen++;

    // Using index from for loop in find, check
    // all the characters in substring for matches
    for(int i = idx; i < strInLen; i++)
    {
        // If there is not a match before end of
        // substring ends, return false
        if(str[i] != strIn[i])
            substring = false;      
    }
    return substring;
}