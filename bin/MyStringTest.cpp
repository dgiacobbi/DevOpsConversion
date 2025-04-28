/*
 Name: David Giacobbi
 Class: CPSC-334
 Assignment: systemd-counter
 Description: This program handles performs basic unit testing on 
              the counter service C++ program.
*/

#include "MyString.cpp"
#include <gtest/gtest.h>


TEST(myStrlen1, ValidLength){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_EQ(str1.myStrlen(), 7);
    ASSERT_EQ(str2->myStrlen(), 7);
}


TEST(myStrlen2, InvalidLength){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_FALSE(str1.myStrlen()==8);
    ASSERT_FALSE(str2->myStrlen()==8);
}


TEST(isEqual1, ValidEqual){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_TRUE(str1.isEqual("testing"));
    ASSERT_TRUE(str2->isEqual("testing"));
}


TEST(isEqual2, InvalidEqual){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_FALSE(str1.isEqual("test"));
    ASSERT_FALSE(str2->isEqual("test"));
}


TEST(find1, ValidFind){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_EQ(str1.find("ing"), 4);
    ASSERT_EQ(str2->find("ing"), 4);
}


TEST(find2, InvalidFind){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_EQ(str1.find("ont"), -1);
    ASSERT_EQ(str2->find("ont"), -1);
}


TEST(isSub1, ValidSub){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_TRUE(str1.isSub("ing", 4));
    ASSERT_TRUE(str2->isSub("ing", 4));
}


TEST(isSub2, InvalidSub){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    ASSERT_FALSE(str1.isSub("tl", 0));
    ASSERT_FALSE(str2->isSub("tl", 0));
}


TEST(concat1, ValidConcat){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    str1.concat("this");
    str2->concat("this");
    ASSERT_TRUE(str1.isEqual("testingthis"));
    ASSERT_TRUE(str2->isEqual("testingthis"));  
}


TEST(concat2, InvalidConcat){
    MyString str1("testing");
    MyString* str2 = new MyString("testing");
    str1.concat("thi");
    str2->concat("thi");
    ASSERT_FALSE(str1.isEqual("testingthis"));
    ASSERT_FALSE(str2->isEqual("testingthis")); 
}


int main(int argc, char **argv){
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}