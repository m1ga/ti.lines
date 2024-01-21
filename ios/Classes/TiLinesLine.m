//
//  TiLinesViewProxy.m
//  ti.lines
//
//  Created by miga on 21.01.24.
//

#import "TiLinesLine.h"
#import "TiUtils.h"
#import "UIKit/UIBezierPath.h"
#import "UIBezierPath+Smoothing.h"

@implementation TiLinesLine

- (void) initializeState {
    square = [[UIView alloc] initWithFrame:[self frame]];
    [self addSubview:square];
    [super initializeState];
}

- (void) frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds {
    [TiUtils setView:square positionRect:bounds];
}

- (void)setColor_:(id) color{
    square.backgroundColor = [[TiUtils colorValue:color]_color];
}

- (void)setValues_:(id) value{
    ENSURE_ARRAY(value);
    values = value;
}

- (void)setLineWidth_:(id) value{
    lineWidth = [TiUtils intValue:value];
}
- (void)setMaxValue_:(id) value{
    maxValue = [TiUtils intValue:value];
}
- (void)setLineColor_:(id) value{
    lineColor = [TiUtils colorValue:value];
}
- (void)setLineType_:(id) value{
    lineType = [TiUtils intValue:value];
}
- (void)setStartAt_:(id) value{
    startAt = [TiUtils intValue:value];
}
-(void)drawRect:(CGRect)rect {
    UIBezierPath *bezierPath = [UIBezierPath bezierPath];
    
    CGFloat stepX = square.bounds.size.width / (values.count - 1);
    CGFloat startX = 0;
    CGFloat startPosition = square.bounds.size.height * 0.5;
    if (startAt == 1) {
        // start at bottom
        startPosition = square.bounds.size.height;
    } else {
        // start in center
        maxValue /= 0.5;
    }
    
    // starting point
    CGFloat hPoint = [TiUtils intValue: values[0]];
    hPoint = (square.bounds.size.height / maxValue) * hPoint;
    [bezierPath moveToPoint:(CGPointMake(0, -hPoint + startPosition)) ];
    
    // draw line
    for (NSInteger i = 0; i < values.count; i++) {
        CGFloat hPoint = [TiUtils intValue: values[i]];
        hPoint = (square.bounds.size.height / maxValue) * hPoint;
        [bezierPath addLineToPoint: CGPointMake(startX, -hPoint + startPosition)];
        startX += stepX;
    }
    
    CAShapeLayer *shapeLayer = [CAShapeLayer layer];
    if (lineType == 0) {
        // smooth path
        UIBezierPath *smoothPath = [bezierPath smoothedPathWithGranularity: 10 minY:0 maxY:100];
        shapeLayer.path = [smoothPath CGPath];
    } else {
        // straight path
        shapeLayer.path = [bezierPath CGPath];
    }
    shapeLayer.strokeColor = [lineColor _color].CGColor;
    shapeLayer.lineWidth = lineWidth;
    shapeLayer.fillColor = [[UIColor clearColor] CGColor];
    
    [square.layer addSublayer:shapeLayer];
}

@end
