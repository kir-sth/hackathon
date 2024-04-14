import React from 'react';
import './ItemPost.css';

/**
 * Компонента ItemPost
 * @kind component
 */
export const ItemPost = React.memo(function ItemPost({
    title,
    text,
    imgSrc,
    link,
}) {
    return (
        <div className="ItemPost">
            {imgSrc && <img className='ItemPost__img' src={imgSrc} alt='Тут должна быть картинка но что то сломалось'/>}
            <div className='ItemPost__content'>
                {title && <h3 className='ItemPost__title'>{title}</h3>}
                {text?.length && (
                    <div className='ItemPost__text'>
                        {text.map(paragraph => <p className='ItemPost__paragraph'>{paragraph}</p>)}
                    </div>
                )}
                {link && <a className='ItemPost__link' href={link.src}>{link.text || link.src}</a>}
            </div>
        </div>
    );
});