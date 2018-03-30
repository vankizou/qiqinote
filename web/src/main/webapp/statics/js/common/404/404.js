/**
 * Created by vanki on 2017/5/18.
 */

(function ($) {
    "use strict";
    var mainApp = {

        main_fun: function () {
            var count = new countUp("error-link", 88, 404, 0, 5); //CHANGE 404 TO THE ERROR VALUE AS YOU WANT
            count.start();
        },

        initialization: function () {
            mainApp.main_fun();

        }

    }
    // Initializing ///

    $(document).ready(function () {
        mainApp.main_fun();
    });

}(jQuery));