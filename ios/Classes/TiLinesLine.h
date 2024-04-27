//
//  TiLinesViewProxy.h
//  ti.lines
//
//  Created by miga on 21.01.24.
//

#import "TiUIView.h"

@interface TiLinesLine : TiUIView {
    UIView *square;
    NSInteger startAt;
    NSInteger lineWidth;
    NSInteger lineType;
    NSInteger dotSize;
    NSInteger maxValue;
    NSInteger yLines;
    TiColor *lineColor;
    TiColor *backgroundColor;
    TiColor *dotColor;
    TiColor *yLineColor;
    NSArray *values;
    bool showDots;
    
    @private
    UIColor *bgColor;
    CAShapeLayer *circleLayer;
}

#pragma mark Public APIs
- (void)redraw:(id)args;
- (void)clear:(id)args;

@end
