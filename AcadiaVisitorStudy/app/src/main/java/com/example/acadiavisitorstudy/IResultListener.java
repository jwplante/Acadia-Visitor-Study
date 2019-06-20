package com.example.acadiavisitorstudy;

public interface IResultListener {
    /*
    * Interface to listen to the result of POST requests. Implement this method and
    * you can use the ILocationProcessor interface
    * @param - result should be true if operation is successful or false if not
    */
    void onSubmit(boolean result);
}
