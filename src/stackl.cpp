//Stack implemented using List and inheritance
#include <iostream>
using namespace std;

#include "stackl.h"

Stack::Stack() : List()
{
}

void Stack::Push(itemType newItem)
{ 
    PutItemH(newItem);
}

void Stack::Pop()
{
    DeleteItemH();
}

itemType Stack::Peek()
{
    return GetItemH(); 
}