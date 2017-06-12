
'use strict';

// -------------------------------------
//   Task: Watch: Public
// -------------------------------------

module.exports = function(gulp) {
  return function() {
    gulp.watch('./content/**/*.scss', ['compile:sass']);
    gulp.watch(['./content/_index.html','./content/_index-inline-loading-script.js','./content/index-inline.scss'],
    ['compile:index']);
  };
};

