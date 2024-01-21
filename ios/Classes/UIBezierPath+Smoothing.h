#import <UIKit/UIKit.h>

@interface UIBezierPath (Smoothing)

- (UIBezierPath*)smoothedPathWithGranularity:(NSInteger)granularity minY:(CGFloat)minY maxY:(CGFloat)maxY;

@end
